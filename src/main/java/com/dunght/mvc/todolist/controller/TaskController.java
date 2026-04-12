package com.dunght.mvc.todolist.controller;

import com.dunght.mvc.todolist.dto.TaskDto;
import com.dunght.mvc.todolist.entity.Task;
import com.dunght.mvc.todolist.entity.User;
import com.dunght.mvc.todolist.entity.Workspace;
import com.dunght.mvc.todolist.service.TaskService;
import com.dunght.mvc.todolist.service.UserService;
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
    private UserService userService;
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
        List<Workspace> workspaces = workspaceService.getAllWorkspacesForUser(acc.getUserId());
        model.addAttribute("tasks", tasks);
        model.addAttribute("workspace", workspace);
        model.addAttribute("currentUser", acc);
        model.addAttribute("workspaces", workspaces);
        model.addAttribute("currentWorkspaceId", workspaceId);
        return "task";
    }

    @PostMapping("/task/create")
    public String createTask(TaskDto taskDto, @RequestParam("workspaceId") Integer workspaceId, RedirectAttributes redirectAttributes, HttpSession session) {
        User acc = (User) session.getAttribute("user");
        if (acc == null) {
            return "redirect:/login";
        }

        // Find the assigned user by username
        User assignedUser = userService.findByEmail(taskDto.getEmail());
        if (assignedUser == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng");
            return "redirect:/workspace/" + workspaceId + "/task";
        }

        Workspace workspace = workspaceService.getWorkspaceById(workspaceId);

        taskService.createTask(taskDto, workspaceId, assignedUser.getUserId());
        return "redirect:/workspace/" + workspaceId + "/task";
    }

    @PostMapping("/task/delete")
    public String deleteTask(@RequestParam("taskId") Integer taskId, @RequestParam("workspaceId") Integer workspaceId, HttpSession session, RedirectAttributes redirectAttributes) {
        User acc = (User) session.getAttribute("user");
        if (acc == null) {
            return "redirect:/login";
        }

        try {
            taskService.deleteTask(taskId, acc.getUserId());
            redirectAttributes.addFlashAttribute("message", "Xóa task thành công");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/workspace/" + workspaceId + "/task";
    }

    @PostMapping("/task/update")
    public String updateTask(@RequestParam("taskId") Integer taskId, TaskDto taskDto, @RequestParam("workspaceId") Integer workspaceId, RedirectAttributes redirectAttributes, HttpSession session) {
        User acc = (User) session.getAttribute("user");
        if (acc == null) {
            return "redirect:/login";
        }

        try {
            taskService.updateTask(taskId, taskDto, acc.getUserId());
            redirectAttributes.addFlashAttribute("message", "Cập nhật task thành công");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/workspace/" + workspaceId + "/task";
    }
}
