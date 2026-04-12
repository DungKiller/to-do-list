package com.dunght.mvc.todolist.service;

import com.dunght.mvc.todolist.dto.WorkspaceDto;
import com.dunght.mvc.todolist.entity.Workspace;

import java.util.List;

public interface WorkspaceService {

    List<Workspace> getWorkspacesByUserId(Integer userId);
    Workspace getWorkspaceById(Integer workspaceId);
    void createWorkspace(String name, Integer userId);
}
