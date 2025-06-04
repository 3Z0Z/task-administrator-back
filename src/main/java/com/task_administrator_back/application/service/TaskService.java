package com.task_administrator_back.application.service;

import com.task_administrator_back.application.dto.request.CreateTaskDTO;
import com.task_administrator_back.application.dto.request.EditTaskDTO;
import com.task_administrator_back.application.dto.response.TaskDTO;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface TaskService {

    void createTask(ObjectId userId, ObjectId projectId, CreateTaskDTO request);

    TaskDTO getTaskById(ObjectId userId, ObjectId projectId, ObjectId taskId);

    Page<TaskDTO> getAllProjectTasks(ObjectId userId, ObjectId projectId, int page, int size);

    void editTask(ObjectId userId, ObjectId projectId, ObjectId taskId, EditTaskDTO request);

    void eliminateTask(ObjectId userId, ObjectId projectId, ObjectId taskId);

    void eliminateTaskForProject(ObjectId projectId);

}
