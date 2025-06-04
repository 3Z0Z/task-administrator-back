package com.task_administrator_back.auth.service;

import com.task_administrator_back.auth.dto.request.LoginRequestDTO;
import com.task_administrator_back.auth.dto.request.RegisterRequestDTO;
import com.task_administrator_back.auth.dto.response.LoginResponseDTO;
import com.task_administrator_back.auth.dto.response.TokenResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    void registerNewUser(RegisterRequestDTO request);

    LoginResponseDTO login(LoginRequestDTO request);

    TokenResponseDTO refreshToken(HttpServletRequest request);

    void logout(String userId);

}
