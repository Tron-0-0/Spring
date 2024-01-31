package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.mapper.ShowUserMapper;

@Controller
@RequestMapping("/user")
public class UserController {

    private final ShowUserMapper showUserMapper;

    @Autowired
    public UserController(ShowUserMapper showUserMapper) {
        this.showUserMapper = showUserMapper;
    }

    @GetMapping
    public String showUserPage(Model model, @AuthenticationPrincipal User user) {

        model.addAttribute("user", showUserMapper.map(user));

        return "user";
    }

}
