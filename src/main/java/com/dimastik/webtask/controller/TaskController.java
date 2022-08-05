package com.dimastik.webtask.controller;

import com.dimastik.webtask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/manager")
    public String manager(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("tasks", taskService.getAllTasksByUserNameId(principal.getName()));
        return "task/manager";
    }


}
