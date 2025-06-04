package com.task_administrator_back.application.service.impl;

import com.task_administrator_back.application.dto.request.CreateProjectDTO;
import com.task_administrator_back.application.dto.request.EditProjectDTO;
import com.task_administrator_back.application.dto.response.ProjectDTO;
import com.task_administrator_back.application.exception.ProjectNotFoundException;
import com.task_administrator_back.application.exception.UnauthorizedActionException;
import com.task_administrator_back.application.model.Project;
import com.task_administrator_back.application.repository.ProjectRepository;
import com.task_administrator_back.application.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public void createProject(ObjectId userId, CreateProjectDTO request) {
        Project newProject = Project.builder()
            .userId(userId)
            .projectName(request.projectName())
            .projectDescription(request.projectDescription())
            .build();
        this.projectRepository.save(newProject);
        log.info("Project created for user {}", userId);
    }

    @Override
    public ProjectDTO getProjectById(ObjectId userId, ObjectId projectId) {
        Project project = this.getProjectById(projectId);
        return this.mapProjectToProjectDTO(project);
    }

    @Override
    public Page<ProjectDTO> getAllProjects(ObjectId userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "projectName");
        Page<Project> projectPage = this.projectRepository.findByUserId(userId, pageable);
        return projectPage.map(this::mapProjectToProjectDTO);
    }

    @Override
    public void editProject(ObjectId userId, ObjectId projectId, EditProjectDTO request) {
        Project project = this.getProjectById(projectId);
        if (!project.getUserId().equals(userId)) {
            throw new UnauthorizedActionException("Unauthorized to apply any modification on project");
        }
        project.setProjectName(request.projectName() != null ? request.projectName() : project.getProjectName());
        project.setProjectDescription(request.projectDescription() != null ? request.projectDescription() : project.getProjectName());
        this.projectRepository.save(project);
        log.info("Project ID {} updated", projectId);
    }

    @Override
    public void eliminateProject(ObjectId userId, ObjectId projectId) {
        Project project = this.getProjectById(projectId);
        if (!project.getUserId().equals(userId)) {
            throw new UnauthorizedActionException("Unauthorized to apply any modification on project");
        }
        this.projectRepository.delete(project);
    }

    @Override
    public void assertProjectBelongsToUser(ObjectId userId, ObjectId projectId) {
        boolean exists = projectRepository.existsByIdAndUserId(projectId, userId);
        if (!exists) {
            throw new UnauthorizedActionException("Project does not belong to the user");
        }
    }

    private Project getProjectById(ObjectId projectId) {
        return this.projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException("Project ID " + projectId + " not found"));
    }

    private ProjectDTO mapProjectToProjectDTO(Project project) {
        return ProjectDTO.builder()
            .id(project.getId().toString())
            .projectName(project.getProjectName())
            .projectDescription(project.getProjectDescription())
            .createdAt(project.getCreatedAt())
            .build();
    }

}
