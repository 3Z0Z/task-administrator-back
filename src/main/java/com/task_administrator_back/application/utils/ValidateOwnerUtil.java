package com.task_administrator_back.application.utils;

import com.task_administrator_back.application.exception.UnauthorizedActionException;
import com.task_administrator_back.application.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateOwnerUtil {

    private final ProjectRepository projectRepository;

    public void assertProjectBelongsToUser(ObjectId userId, ObjectId projectId) {
        boolean exists = this.projectRepository.existsByIdAndUserId(projectId, userId);
        if (!exists) {
            throw new UnauthorizedActionException("Project does not belong to the user");
        }
    }

}
