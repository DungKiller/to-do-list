package com.dunght.mvc.todolist.service.ServiceImpl;

import com.dunght.mvc.todolist.dto.WorkspaceDto;
import com.dunght.mvc.todolist.entity.User;
import com.dunght.mvc.todolist.entity.Workspace;
import com.dunght.mvc.todolist.repository.TaskRepository;
import com.dunght.mvc.todolist.repository.UserRepository;
import com.dunght.mvc.todolist.repository.WorkspaceRepository;
import com.dunght.mvc.todolist.service.WorkspaceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Workspace> getWorkspacesByUserId(Integer userId) {
        return workspaceRepository.findAllByMembers_UserId(userId);
    }

    @Override
    public List<Workspace> getAllWorkspacesForUser(Integer userId) {
        return workspaceRepository.findByUserId(userId);
    }

    @Override
    public Workspace getWorkspaceById(Integer workspaceId) {
        return workspaceRepository.findById(workspaceId).orElse(null);
    }

    @Override
    @Transactional
    public void createWorkspace(String name, Integer userId) {
        if (workspaceRepository.existsByName(name)) {
            throw new RuntimeException("Tên đã tồn tại");
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Workspace workspace = new Workspace();
            workspace.setOwner(user);
            workspace.setName(name);
            workspaceRepository.save(workspace);
            workspace.getMembers().add(user);
            workspaceRepository.save(workspace);
        }

    }

    @Override
    @Transactional
    public void deleteWorkspace(Integer workspaceId, Integer userId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        if (workspace != null && workspace.getOwner().getUserId().equals(userId)) {
            taskRepository.deleteByWorkspace_WorkspaceId(workspaceId);
            workspaceRepository.delete(workspace);
        } else {
            throw new RuntimeException("Không có quyền xóa workspace này");
        }
    }
}
