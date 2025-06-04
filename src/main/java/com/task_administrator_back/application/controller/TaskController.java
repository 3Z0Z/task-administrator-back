package com.task_administrator_back.application.controller;

import com.task_administrator_back.application.dto.request.CreateTaskDTO;
import com.task_administrator_back.application.dto.request.EditTaskDTO;
import com.task_administrator_back.application.dto.response.SuccessResponseDTO;
import com.task_administrator_back.application.dto.response.TaskDTO;
import com.task_administrator_back.application.service.TaskService;
import com.task_administrator_back.shared.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final JwtService jwtService;
    private final TaskService taskService;

    @PostMapping("/{projectId}/create")
    public ResponseEntity<SuccessResponseDTO> createTask(
        HttpServletRequest servletRequest,
        @PathVariable ObjectId projectId,
        @RequestBody @Valid CreateTaskDTO request
    ) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        this.taskService.createTask(new ObjectId(userId), projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponseDTO("Task created successfully"));
    }

    @GetMapping("/{projectId}/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(
        HttpServletRequest servletRequest,
        @PathVariable ObjectId projectId,
        @PathVariable ObjectId taskId
    ) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        TaskDTO response = this.taskService.getTaskById(new ObjectId(userId), projectId, taskId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{projectId}/page")
    public ResponseEntity<Page<TaskDTO>> getAllProjectTasks(
        HttpServletRequest servletRequest,
        @PathVariable ObjectId projectId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        Page<TaskDTO> response = this.taskService.getAllProjectTasks(new ObjectId(userId), projectId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{projectId}/{taskId}")
    public ResponseEntity<SuccessResponseDTO> updateTask(
        HttpServletRequest servletRequest,
        @PathVariable ObjectId projectId,
        @PathVariable ObjectId taskId,
        @RequestBody @Valid EditTaskDTO request
    ) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        this.taskService.editTask(new ObjectId(userId), projectId, taskId, request);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Task updated successfully"));
    }

    @DeleteMapping("/{projectId}/{taskId}")
    public ResponseEntity<SuccessResponseDTO> eliminateTask(
        HttpServletRequest servletRequest,
        @PathVariable ObjectId projectId,
        @PathVariable ObjectId taskId
    ) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        this.taskService.eliminateTask(new ObjectId(userId), projectId, taskId);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Task deleted successfully"));
    }

}

