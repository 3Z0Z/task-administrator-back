package com.task_administrator_back.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("project")
public class Project {

    @Id
    private ObjectId id;

    @Indexed
    private ObjectId userId;

    private String projectName;

    private String projectDescription;

    @Builder.Default
    private Instant createdAt = Instant.now();

}
