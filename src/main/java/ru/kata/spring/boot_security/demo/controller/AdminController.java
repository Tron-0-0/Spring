package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.CreateUserReadDto;
import ru.kata.spring.boot_security.demo.dto.ShowUserReadDto;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.mapper.CreateUserMapper;
import ru.kata.spring.boot_security.demo.mapper.ShowUserMapper;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ShowUserMapper showUserMapper;
    private final CreateUserMapper createUserMapper;

    public AdminController(UserService userService, ShowUserMapper showUserMapper, CreateUserMapper createUserMapper) {
        this.userService = userService;
        this.showUserMapper = showUserMapper;
        this.createUserMapper = createUserMapper;
    }

    @GetMapping
    public String showAdminPage(Model model) {

        List<ShowUserReadDto> userReadList = userService.getAll().stream()
                .map(showUserMapper::map)
                .toList();

        model.addAttribute("users", userReadList);
        return "admin";
    }

    @GetMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String showPageToCreateUser(Model model) {
        model.addAttribute("roles", Role.values());
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute CreateUserReadDto user) {
        userService.save(createUserMapper.map(user));

        return "redirect:/admin";
    }

    @GetMapping("/change")
    public String showChangePage(Model model, @RequestParam("username") String username) {
        model.addAttribute("user", userService.loadUserByUsername(username));
        model.addAttribute("roles", Role.values());

        return "change";
    }

    @PostMapping("/change")
    public String changeUser(@ModelAttribute CreateUserReadDto createUserReadDto) {
        userService.update(createUserMapper.map(createUserReadDto));

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("username") String username) {
        userService.deleteByUsername(username);

        return "redirect:/admin";
    }
}
