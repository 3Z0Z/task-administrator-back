package com.task_administrator_back.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDTO(

    String refreshToken,
    String jwtToken

) { }
