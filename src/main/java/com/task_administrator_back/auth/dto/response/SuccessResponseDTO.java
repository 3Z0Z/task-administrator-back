package com.task_administrator_back.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SuccessResponseDTO(

    @JsonProperty("message")
    String message
    
) { }
