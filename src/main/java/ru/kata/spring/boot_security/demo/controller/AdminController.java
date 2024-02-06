package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.CreateUserReadDto;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.mapper.CreateUserMapper;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CreateUserMapper createUserMapper;

    public AdminController(UserService userService, CreateUserMapper createUserMapper) {
        this.userService = userService;
        this.createUserMapper = createUserMapper;
    }

    @GetMapping
    public String showAdminPage(Model model, Principal principal) {

        model.addAttribute("users", userService.getAll());
        model.addAttribute("admin",userService.findByUsername(principal.getName()));
        model.addAttribute("rolesAll", Role.values());
        model.addAttribute("userToCreating", CreateUserReadDto.emptyObject());

        return "admin";
    }

    @PostMapping("/add")
    public String createUser(CreateUserReadDto createUserReadDto) {
        userService.save(createUserMapper.map(createUserReadDto));

        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") CreateUserReadDto user) {

        userService.update(id, createUserMapper.map(user));

        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return "redirect:/admin";
    }
}
