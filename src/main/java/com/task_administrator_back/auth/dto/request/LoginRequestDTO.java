package com.task_administrator_back.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginRequestDTO(

		@JsonProperty("username")
        @NotNull(message = "is mandatory")
        @Pattern(message = "must have between 2 and 50 characters and no special sing", regexp = "^[a-zA-Z*\\d]{2,50}$")
        String username,

        @JsonProperty("password")
        @NotNull(message = "is mandatory")
        String password

) { }
