package com.dimastik.webtask.controller;

import com.dimastik.webtask.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/adminPanel")
    public String adminPanel(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin";
    }

    @PostMapping("/adminPanel/admin/{id}")
    public String setAdmin(@PathVariable Long id, Model model) {
        userService.setAdmin(id);
        return "redirect:/adminPanel";
    }

    @PostMapping("/adminPanel/user/{id}")
    public String setUser(@PathVariable Long id, Model model) {
        userService.setUser(id);
        return "redirect:/adminPanel";
    }
}
