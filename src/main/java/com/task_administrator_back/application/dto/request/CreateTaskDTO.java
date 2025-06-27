package com.task_administrator_back.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTaskDTO(

    @JsonProperty("task_name")
    @NotBlank(message = "Task Name is required")
    @Size(min = 1, max = 30, message = "Task Name must be between 1 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Task Name must not contain special characters")
    String taskName,

    @JsonProperty("task_description")
    @NotBlank(message = "Task Description is required")
    @Size(min = 10, max = 100, message = "Task Description must be between 10 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9,.\\- ]*$", message = "Task Description must not contain special characters")
    String TaskDescription

) { }
