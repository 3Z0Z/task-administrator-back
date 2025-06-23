package com.task_administrator_back.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EditProjectDTO(

    @JsonProperty("project_name")
    @Size(min = 1, max = 30, message = "Project Name must be between 1 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Project Name must not contain special characters")
    String projectName,

    @JsonProperty("project_description")
    @Size(min = 10, max = 100, message = "Project Description must be between 10 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Project Description must not contain special characters")
    String projectDescription

) { }
