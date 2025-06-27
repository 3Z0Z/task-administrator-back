package com.task_administrator_back.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("task")
public class Task {

    @Id
    private ObjectId id;

    private ObjectId projectId;

    private String taskName;

    private String taskDescription;

    private boolean isCompleted;

    @Builder.Default
    private Instant createdAt = Instant.now();

}
