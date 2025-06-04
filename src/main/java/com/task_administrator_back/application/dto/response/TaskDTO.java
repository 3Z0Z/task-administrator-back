package com.task_administrator_back.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TaskDTO(

    @JsonProperty("id")
    String id,

    @JsonProperty("task_name")
    String taskName,

    @JsonProperty("task_description")
    String taskDescription,

    @JsonProperty("created_at")
    Instant createdAt

) { }
