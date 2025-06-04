package com.task_administrator_back.application.controller;

import com.task_administrator_back.application.dto.request.CreateProjectDTO;
import com.task_administrator_back.application.dto.request.EditProjectDTO;
import com.task_administrator_back.application.dto.response.ProjectDTO;
import com.task_administrator_back.application.dto.response.SuccessResponseDTO;
import com.task_administrator_back.application.service.ProjectService;
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
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final JwtService jwtService;
    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDTO> createProject(HttpServletRequest servletRequest, @RequestBody @Valid CreateProjectDTO request) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        this.projectService.createProject(new ObjectId(userId), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponseDTO("Project created successfully"));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(HttpServletRequest servletRequest, @PathVariable ObjectId projectId) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        ProjectDTO response = this.projectService.getProjectById(new ObjectId(userId), projectId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProjectDTO>> getAllUserProjects(
        HttpServletRequest servletRequest,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        Page<ProjectDTO> response = this.projectService.getAllProjects(new ObjectId(userId), page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<SuccessResponseDTO> updateProject(
        HttpServletRequest servletRequest,
        @PathVariable ObjectId projectId,
        @RequestBody @Valid EditProjectDTO request
    ) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        this.projectService.editProject(new ObjectId(userId), projectId, request);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Project updated successfully"));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<SuccessResponseDTO> eliminateProject(HttpServletRequest servletRequest, @PathVariable ObjectId projectId) {
        String userId = this.jwtService.extractUserIdFromRequest(servletRequest);
        this.projectService.eliminateProject(new ObjectId(userId), projectId);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Project deleted successfully"));
    }

}
