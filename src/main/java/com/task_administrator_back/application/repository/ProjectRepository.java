package com.task_administrator_back.application.repository;

import com.task_administrator_back.application.model.Project;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId> {

    Page<Project> findByUserId(ObjectId userId, Pageable pageable);

    boolean existsByIdAndUserId(ObjectId id, ObjectId userId);

}
