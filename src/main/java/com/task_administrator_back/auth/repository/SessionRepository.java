package com.task_administrator_back.auth.repository;

import com.task_administrator_back.auth.model.Session;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<Session, ObjectId> {

    Optional<Session> findByUserId(ObjectId userId);

    Optional<Session> findByRefreshToken(String refreshToken);

}
