package com.dunght.mvc.todolist.service;

import com.dunght.mvc.todolist.dto.TaskDto;
import com.dunght.mvc.todolist.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findByWorkspace_WorkspaceId(Integer workspaceId);
    List<Task> getTasksByWorkspaceId(Integer workspaceId);
    void createTask(TaskDto taskDto, Integer workspaceId, Integer userId);
}
