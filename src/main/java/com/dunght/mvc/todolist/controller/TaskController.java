package com.dunght.mvc.todolist.controller;

import com.dunght.mvc.todolist.dto.TaskDto;
import com.dunght.mvc.todolist.entity.Task;
import com.dunght.mvc.todolist.entity.User;
import com.dunght.mvc.todolist.entity.Workspace;
import com.dunght.mvc.todolist.service.TaskService;
import com.dunght.mvc.todolist.service.WorkspaceService;
import com.dunght.mvc.todolist.repository.UserRepository;
import com.dunght.mvc.todolist.repository.WorkspaceRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @GetMapping("/workspace/{workspaceId}/task")
    public String taskList(@PathVariable("workspaceId") int workspaceId, Model model, HttpSession session) {
        User acc = (User) session.getAttribute("user");
        if (acc == null) {
            return "redirect:/login";
        }

        Workspace workspace = workspaceService.getWorkspaceById(workspaceId);
        List<Task> tasks = taskService.getTasksByWorkspaceId(workspaceId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("workspace", workspace);
        model.addAttribute("currentUser", acc);
        return "task";
    }

    @PostMapping("/task/create")
    public String createTask(TaskDto taskDto, @RequestParam("workspaceId") Integer workspaceId, RedirectAttributes redirectAttributes, HttpSession session) {
        User acc = (User) session.getAttribute("user");
        if (acc == null) {
            return "redirect:/login";
        }

        // Find the assigned user by username
        User assignedUser = userRepository.findByUsername(taskDto.getUsername());
        if (assignedUser == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng với username: " + taskDto.getUsername());
            return "redirect:/workspace/" + workspaceId + "/task";
        }

        // Add the assigned user to the workspace members if not already a member
        Workspace workspace = workspaceService.getWorkspaceById(workspaceId);
        if (!workspace.getMembers().contains(assignedUser)) {
            workspace.getMembers().add(assignedUser);
            workspaceRepository.save(workspace);
        }

        taskService.createTask(taskDto, workspaceId, assignedUser.getUserId());
        return "redirect:/workspace/" + workspaceId + "/task";
    }
}
