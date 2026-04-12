package com.dunght.mvc.todolist.controller;

import com.dunght.mvc.todolist.entity.User;
import com.dunght.mvc.todolist.entity.Workspace;
import com.dunght.mvc.todolist.service.WorkspaceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/workspace")
    public String workspaceList(Model model, HttpSession session) {
        User acc = (User) session.getAttribute("user");

        if (acc == null) {
            return "redirect:/login";
        }

        List<Workspace> workspaces = workspaceService.getWorkspacesByUserId(acc.getUserId());
        model.addAttribute("workspaces", workspaces);
        return "workspace";
    }

    @PostMapping("/workspace/create")
    public String createWorkspace(@RequestParam("name") String name, HttpSession session,
                                  RedirectAttributes redirectAttributes, Model model) {
        User acc = (User) session.getAttribute("user");
        if (acc == null) {
            return "redirect:/login";
        }

        try {
            workspaceService.createWorkspace(name, acc.getUserId());
            redirectAttributes.addFlashAttribute("message", "Tạo không gian thành công");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "redirect:/workspace";
    }

    @PostMapping("/workspace/delete")
    public String deleteWorkspace(@RequestParam("workspaceId") Integer workspaceId, HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        User acc = (User) session.getAttribute("user");
        if (acc == null) {
            return "redirect:/login";
        }

        try {
            workspaceService.deleteWorkspace(workspaceId, acc.getUserId());
            redirectAttributes.addFlashAttribute("message", "Xóa workspace thành công");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/workspace";
    }
}
