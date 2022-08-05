package com.dimastik.webtask.controller;

import com.dimastik.webtask.model.User;
import com.dimastik.webtask.service.UserService;
import com.dimastik.webtask.validate.RegisterFormValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@Slf4j
public class WebController {

    private final UserService userService;

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid User form,
                               BindingResult result, Model model, HttpServletRequest request) {
        new RegisterFormValid().validForm(form, result, userService);
        if (result.hasErrors()) {
            return "registration";
        }
        userService.createUser(form.getUsername(), form.getPassword());
        try {
            request.login(form.getUsername(), form.getPassword());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/account";
    }

    @GetMapping("/account")
    public String account(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "account";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
