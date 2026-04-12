package com.dunght.mvc.todolist.service.ServiceImpl;

import com.dunght.mvc.todolist.dto.TaskDto;
import com.dunght.mvc.todolist.entity.Task;
import com.dunght.mvc.todolist.entity.User;
import com.dunght.mvc.todolist.entity.Workspace;
import com.dunght.mvc.todolist.repository.TaskRepository;
import com.dunght.mvc.todolist.repository.UserRepository;
import com.dunght.mvc.todolist.repository.WorkspaceRepository;
import com.dunght.mvc.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Task> findByWorkspace_WorkspaceId(Integer workspaceId) {
        return taskRepository.findByWorkspace_WorkspaceId(workspaceId);
    }

    @Override
    public List<Task> getTasksByWorkspaceId(Integer workspaceId) {
        // Gọi hàm Query mới để lấy cả thông tin User thực hiện
        return taskRepository.findAllTasksWithUserByWorkspaceId(workspaceId);
    }

    @Override
    public void createTask(TaskDto taskDto, Integer workspaceId, Integer userId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (workspace != null) {
            if (!workspace.getMembers().contains(user)) {
                workspace.getMembers().add(user);
                workspaceRepository.save(workspace);
            }
            Task task = new Task();
            task.setNote(taskDto.getNote());
            task.setPriority(taskDto.getPriority());
            task.setStatus(taskDto.getStatus());
            task.setEndDate(taskDto.getEndDate());
            task.setTitle(taskDto.getTitle());
            task.setWorkspace(workspace);
            task.setUser(user);
            taskRepository.save(task);
        }
    }

    @Override
    public void deleteTask(Integer taskId, Integer userId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            taskRepository.delete(task);
        } else {
            throw new RuntimeException("Task không tồn tại");
        }
    }

    @Override
    public void updateTask(Integer taskId, TaskDto taskDto, Integer userId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            // Find the assigned user by email
            User assignedUser = userRepository.findByEmail(taskDto.getEmail());
            if (assignedUser == null) {
                throw new RuntimeException("Không tìm thấy người dùng");
            }
            task.setTitle(taskDto.getTitle());
            task.setNote(taskDto.getNote());
            task.setPriority(taskDto.getPriority());
            task.setStatus(taskDto.getStatus());
            task.setEndDate(taskDto.getEndDate());
            task.setUser(assignedUser);
            taskRepository.save(task);
        } else {
            throw new RuntimeException("Task không tồn tại");
        }
    }
}
