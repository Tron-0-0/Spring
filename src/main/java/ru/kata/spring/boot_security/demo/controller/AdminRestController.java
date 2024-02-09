package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.dto.CreateUserReadDto;
import ru.kata.spring.boot_security.demo.dto.ShowUserReadDto;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.mapper.CreateUserMapper;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
public class AdminRestController {

    private final UserService userService;
    private final CreateUserMapper createUserMapper;

    @Autowired
    public AdminRestController(UserService userService, CreateUserMapper createUserMapper) {
        this.userService = userService;
        this.createUserMapper = createUserMapper;
    }

    @GetMapping("/api/admin")
    public ResponseEntity<List<ShowUserReadDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ModelAndView adminPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping("/api/admin/{id}")
    public ShowUserReadDto getUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping("/api/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = List.of(Role.values());

        return new ResponseEntity<>(roles, HttpStatus.OK);
    }


    @PostMapping("/api/admin")
    public ResponseEntity<User> saveNewUser(@RequestBody CreateUserReadDto createUserReadDto) {
        userService.save(createUserMapper.map(createUserReadDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/admin")
    public ResponseEntity<User> updateUser(@RequestBody CreateUserReadDto createUserReadDto) {
        User newUser = createUserMapper.map(createUserReadDto);
        userService.update(newUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
