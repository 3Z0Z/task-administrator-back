package com.task_administrator_back.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDTO(

	@JsonProperty("username")
    @NotNull(message = "is mandatory")
    @Pattern(message = "must have between 2 and 50 characters and no special sing", regexp = "^[a-zA-Z*\\d]{2,50}$")
    String username,

    @JsonProperty("password")
    @NotNull(message = "is mandatory")
    @Pattern(message = "must have at least 10 and 50 characters, a number and a special sing", regexp = "^(?=.*\\d)(?=.*[-_*?!@/().#=])[A-Za-z\\d-_*?!@/().#=]{10,50}$")
    String password,

    @JsonProperty("email")
    @Email(message = "invalid email format")
    @NotNull(message = "is mandatory")
    String email

) { }
