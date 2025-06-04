package com.task_administrator_back.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateProjectDTO(

    @JsonProperty("project_name")
    @NotBlank(message = "Project Name is required")
    @Size(min = 1, max = 30, message = "Project Name must be between 1 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Project Name must not contain special characters")
    String projectName,

    @JsonProperty("project_description")
    @NotBlank(message = "Project Description is required")
    @Size(min = 10, max = 100, message = "Project Description must be between 10 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Project Description must not contain special characters")
    String projectDescription

) { }
