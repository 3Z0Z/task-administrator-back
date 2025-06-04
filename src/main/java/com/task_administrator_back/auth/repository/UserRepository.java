package com.task_administrator_back.auth.repository;

import com.task_administrator_back.auth.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

}
