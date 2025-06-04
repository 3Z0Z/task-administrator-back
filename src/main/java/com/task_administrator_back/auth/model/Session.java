package com.task_administrator_back.auth.model;

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
@Document("session")
public class Session {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private ObjectId userId;

    private String sessionToken;

    private Instant sessionTokenGenerationTime;

    private String refreshToken;

    private boolean refreshTokenRevoked;

    private Instant refreshTokenCreatedAt;

    private Instant refreshTokenExpiresAt;

}
