package com.task_administrator_back.application.service;

import com.task_administrator_back.application.dto.request.CreateProjectDTO;
import com.task_administrator_back.application.dto.request.EditProjectDTO;
import com.task_administrator_back.application.dto.response.ProjectDTO;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface ProjectService {

    void createProject(ObjectId userId, CreateProjectDTO request);

    ProjectDTO getProjectById(ObjectId userId, ObjectId projectId);

    Page<ProjectDTO> getAllProjects(ObjectId userId, int page, int size);

    void editProject(ObjectId userId, ObjectId projectId, EditProjectDTO request);

    void eliminateProject(ObjectId userId, ObjectId projectId);

    void assertProjectBelongsToUser(ObjectId userId, ObjectId projectId);

}
