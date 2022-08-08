package com.dimastik.webtask.controller;

import com.dimastik.webtask.model.Task;
import com.dimastik.webtask.service.TaskService;
import com.dimastik.webtask.validate.TaskFormValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@Slf4j
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

    @GetMapping("/task/{id}")
    public String getTaskById(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("task", taskService.getTaskById(id, principal.getName()));
        return "/task/task";
    }

    @PostMapping("/task/{id}")
    public String deleteTask(@PathVariable Long id, Model model, Principal principal) {
        taskService.deleteTask(id, principal.getName());
        return "redirect:/manager";
    }

    @GetMapping("/task/taskEditor/{id}")
    public String editor(@PathVariable Long id, Model model, Principal principal) {
        Task taskById = taskService.getTaskById(id, principal.getName());
        model.addAttribute("task", taskById);
        return "/task/taskEditor";
    }

    @PostMapping("/task/taskEditor")
    public String editorSave(@ModelAttribute("task") Task task,
                             Model model,
                             Principal principal,
                             BindingResult bindingResult) {
        new TaskFormValid().validForm(task, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/task/taskEditor";
        }
        taskService.saveTask(task, principal);
        model.addAttribute("save", "Сохранено!");
        return "/task/taskEditor";
    }

    @GetMapping("task/taskEditor")
    public String editor(Model model) {
        model.addAttribute("task", new Task());
        return "/task/taskEditor";
    }



}
