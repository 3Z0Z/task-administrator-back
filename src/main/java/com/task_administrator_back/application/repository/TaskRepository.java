package com.task_administrator_back.application.repository;

import com.task_administrator_back.application.model.Task;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, ObjectId> {

    Page<Task> findByProjectId(ObjectId projectId, Pageable pageable);

}
