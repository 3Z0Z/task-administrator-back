package com.task_administrator_back.application.service.impl;

import com.task_administrator_back.application.dto.request.CreateTaskDTO;
import com.task_administrator_back.application.dto.request.EditTaskDTO;
import com.task_administrator_back.application.dto.response.TaskDTO;
import com.task_administrator_back.application.exception.TaskNotFoundException;
import com.task_administrator_back.application.exception.UnauthorizedActionException;
import com.task_administrator_back.application.model.Task;
import com.task_administrator_back.application.repository.TaskRepository;
import com.task_administrator_back.application.service.TaskService;
import com.task_administrator_back.application.utils.ValidateOwnerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ValidateOwnerUtil validateOwnerUtil;

    @Override
    public void createTask(ObjectId userId, ObjectId projectId, CreateTaskDTO request) {
        this.validateOwnerUtil.assertProjectBelongsToUser(userId, projectId);
        Task newTask = Task.builder()
            .projectId(projectId)
            .taskName(request.taskName())
            .taskDescription(request.TaskDescription())
            .build();
        this.taskRepository.save(newTask);
        log.info("New task created for project {}", projectId);
    }

    @Override
    public TaskDTO getTaskById(ObjectId userId, ObjectId projectId, ObjectId taskId) {
        this.validateOwnerUtil.assertProjectBelongsToUser(userId, projectId);
        Task task = this.getTaskById(taskId);
        return this.mapTaskToTaskDto(task);
    }

    @Override
    public Page<TaskDTO> getAllProjectTasks(ObjectId userId, ObjectId projectId, int page, int size) {
        this.validateOwnerUtil.assertProjectBelongsToUser(userId, projectId);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "taskName");
        Page<Task> taskPage = this.taskRepository.findByProjectId(projectId, pageable);
        return taskPage.map(this::mapTaskToTaskDto);
    }

    @Override
    public void editTask(ObjectId userId, ObjectId projectId, ObjectId taskId, EditTaskDTO request) {
        this.validateOwnerUtil.assertProjectBelongsToUser(userId, projectId);
        Task task = this.getTaskById(taskId);
        task.setTaskName(request.taskName() != null ? request.taskName() : task.getTaskName());
        task.setTaskDescription(request.taskDescription() != null ? request.taskDescription() : task.getTaskDescription());
        this.taskRepository.save(task);
        log.info("Task ID {} updated", task);
    }

    @Override
    public void eliminateTask(ObjectId userId, ObjectId projectId, ObjectId taskId) {
        this.validateOwnerUtil.assertProjectBelongsToUser(userId, projectId);
        Task task = this.getTaskById(taskId);
        if (!task.getProjectId().equals(projectId)) {
            throw new UnauthorizedActionException("Unauthorized to apply any modification on task");
        }
        this.taskRepository.delete(task);
    }

    @Override
    public void eliminateTaskForProject(ObjectId projectId) {
        List<Task> taskList = this.taskRepository.findByProjectId(projectId);
        this.taskRepository.deleteAll(taskList);
    }

    private Task getTaskById(ObjectId taskId) {
        return this.taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with ID " + taskId));
    }

    private TaskDTO mapTaskToTaskDto(Task task) {
        return TaskDTO.builder()
            .id(task.getId().toString())
            .taskName(task.getTaskName())
            .taskDescription(task.getTaskDescription())
            .createdAt(task.getCreatedAt())
            .build();
    }

}
