package com.task_administrator_back.auth.service.impl;

import com.task_administrator_back.shared.service.JwtService;
import com.task_administrator_back.auth.dto.request.LoginRequestDTO;
import com.task_administrator_back.auth.dto.request.RegisterRequestDTO;
import com.task_administrator_back.auth.dto.response.LoginResponseDTO;
import com.task_administrator_back.auth.dto.response.TokenResponseDTO;
import com.task_administrator_back.auth.exception.AuthenticationFailedException;
import com.task_administrator_back.auth.exception.RefreshTokenException;
import com.task_administrator_back.auth.exception.UserRegistrationException;
import com.task_administrator_back.auth.model.Session;
import com.task_administrator_back.auth.model.User;
import com.task_administrator_back.auth.repository.SessionRepository;
import com.task_administrator_back.auth.repository.UserRepository;
import com.task_administrator_back.auth.service.AuthenticationService;
import com.task_administrator_back.auth.utils.EncryptionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void registerNewUser(RegisterRequestDTO request) {
        if (this.userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new UserRegistrationException("Username or email already register");
        }
        User newUser = User.builder()
            .username(request.username())
            .password(this.passwordEncoder.encode(this.encryptionService.decrypt(request.password())))
            .email(request.email())
            .build();
        this.userRepository.save(newUser);
        log.info("--> registerNewUser: registration successful, username {}", request.username());
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), this.encryptionService.decrypt(request.password())));
        } catch(AuthenticationException e) {
            throw new AuthenticationFailedException("Username or password incorrect");
        }
        User user = this.userRepository.findByUsername(request.username())
            .orElseThrow(() -> new UsernameNotFoundException("Unable to find the username " + request.username()));
        String sessionToken = this.generateAccessToken(user);
        String refreshToken = this.generateRefreshToken(user);
        Optional<Session> existingSession = this.sessionRepository.findByUserId(user.getId());
        if (existingSession.isPresent()) {
            existingSession.get().setSessionToken(sessionToken);
            existingSession.get().setSessionTokenGenerationTime(Instant.now());
            existingSession.get().setRefreshToken(refreshToken);
            existingSession.get().setRefreshTokenRevoked(false);
            existingSession.get().setRefreshTokenCreatedAt(Instant.now());
            existingSession.get().setRefreshTokenExpiresAt(this.jwtService.extractExpiration(refreshToken).toInstant());
            this.sessionRepository.save(existingSession.get());
        } else {
            Session newSession = Session.builder()
                .userId(user.getId())
                .sessionToken(sessionToken)
                .sessionTokenGenerationTime(Instant.now())
                .refreshToken(refreshToken)
                .refreshTokenRevoked(false)
                .refreshTokenCreatedAt(Instant.now())
                .refreshTokenExpiresAt(this.jwtService.extractExpiration(refreshToken).toInstant())
                .build();
            this.sessionRepository.save(newSession);
        }
        log.info("-->loginService: login success, username {}", user.getUsername());
        return LoginResponseDTO.builder().refreshToken(refreshToken).jwtToken(sessionToken).build();
    }

    @Override
    public TokenResponseDTO refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new RefreshTokenException("No cookies found in the request");
        Cookie cookieRefreshToken = Arrays.stream(request.getCookies())
            .filter(cookie -> "refresh_token".equals(cookie.getName()))
            .findFirst()
            .orElseThrow(() -> new RefreshTokenException("Refresh Token cookie not found"));
        String refreshToken = cookieRefreshToken.getValue();
        if (this.jwtService.isTokenExpired(refreshToken)) throw new RefreshTokenException("Refresh Token is expired");
        Session session = this.sessionRepository.findByRefreshToken(refreshToken)
            .filter(rt -> !rt.isRefreshTokenRevoked())
            .orElseThrow(() -> new RefreshTokenException("Token not found"));
        User user = this.userRepository.findById(new ObjectId(this.jwtService.extractUserId(refreshToken)))
            .orElseThrow(() -> new UsernameNotFoundException("Unable to find the user"));
        String newSessionToken = this.generateAccessToken(user);
        session.setSessionToken(newSessionToken);
        this.sessionRepository.save(session);
        return new TokenResponseDTO(newSessionToken);
    }

    @Override
    public void logout(String userId) {
        User userToLogout = this.userRepository.findById(new ObjectId(userId))
            .orElseThrow(() -> new UsernameNotFoundException("Username not found:" + userId));
        Optional<Session> existingSession = this.sessionRepository.findByUserId(userToLogout.getId());
        if (existingSession.isPresent()) {
            existingSession.get().setRefreshTokenRevoked(true);
            this.sessionRepository.save(existingSession.get());
        }
        log.info("-->logout: logout successful, userId {}", userId);
    }

    private String generateAccessToken(User user) {
        return this.jwtService.generateToken(user, false); //set to 15 min
    }

    private String generateRefreshToken(User user) {
        return this.jwtService.generateToken(user, true); //set to 30 days
    }

}
