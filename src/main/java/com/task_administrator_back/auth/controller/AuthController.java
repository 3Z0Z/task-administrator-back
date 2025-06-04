package com.task_administrator_back.auth.controller;

import com.task_administrator_back.shared.service.JwtService;
import com.task_administrator_back.auth.dto.request.LoginRequestDTO;
import com.task_administrator_back.auth.dto.request.RegisterRequestDTO;
import com.task_administrator_back.auth.dto.response.LoginResponseDTO;
import com.task_administrator_back.auth.dto.response.SuccessResponseDTO;
import com.task_administrator_back.auth.dto.response.TokenResponseDTO;
import com.task_administrator_back.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> registerUser(@RequestBody @Valid RegisterRequestDTO request) {
        this.authenticationService.registerNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponseDTO("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = this.authenticationService.login(request);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", response.refreshToken())
            .httpOnly(true)
            //.secure(true);
            .path("/")
            .maxAge(30L * 24L * 60L * 60L) //expire in 30 days
            .sameSite("Strict").build();
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(new TokenResponseDTO(response.jwtToken())
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDTO> refreshToken(HttpServletRequest request) {
        TokenResponseDTO tokenResponseDTO = this.authenticationService.refreshToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponseDTO> logout(HttpServletRequest request) {
        String userId = this.jwtService.extractUserIdFromRequest(request);
        this.authenticationService.logout(userId);
        ResponseCookie deleteRefreshCookie = ResponseCookie.from("refresh_token", "")
            .httpOnly(true)
            //.secure(true)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build();
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString())
            .body(new SuccessResponseDTO("Logout successfully")
        );
    }

}
