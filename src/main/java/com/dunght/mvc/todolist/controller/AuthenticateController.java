package com.dunght.mvc.todolist.controller;

import com.dunght.mvc.todolist.dto.UserDto;
import com.dunght.mvc.todolist.entity.User;
import com.dunght.mvc.todolist.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthenticateController {
    @Autowired
    private UserService userService;

    @GetMapping({"/", "/login"})
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
        User user = userService.findByUsernameAndPassword(username, password);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Sai username hoặc mật khẩu");
            return "redirect:/login";
        }

        session.setAttribute("user", user);
        return "redirect:/workspace";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("userDto") UserDto userDto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "signup";
        } else if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("userDto", userDto);
            model.addAttribute("error", "Mật khẩu không khớp");
            return "signup";
        } else {
            try {
                userService.createUser(userDto);
                redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!! Vui lòng đăng nhập lại.");
                return "redirect:/login";
            } catch (Exception ex) {
                model.addAttribute("userDto", userDto);
                model.addAttribute("error", ex.getMessage());
                return "signup";
            }

        }

    }
}
