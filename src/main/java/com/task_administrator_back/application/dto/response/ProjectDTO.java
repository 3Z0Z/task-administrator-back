package com.task_administrator_back.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ProjectDTO(

    @JsonProperty("id")
    String id,

    @JsonProperty("project_name")
    String projectName,

    @JsonProperty("project_description")
    String projectDescription,

    @JsonProperty("created_at")
    Instant createdAt

) { }
