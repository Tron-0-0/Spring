package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.CreateUserReadDto;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CreateUserMapper implements Mapper<User, CreateUserReadDto> {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CreateUserMapper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User map(CreateUserReadDto entity) {
        User user = new User();

        user.setUsername(entity.username());
        user.setAge(entity.age());
        user.setFirstname(entity.firstname());
        user.setLastname(entity.lastname());

        if (!entity.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(entity.password()));
        }

        if (entity.roles().contains(",")) {
            user.setRoles(Arrays.stream(entity.roles().split(","))
                    .map(role -> Role.valueOf("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toSet()));
        } else {
            user.setRoles(Collections.singleton(Role.valueOf("ROLE_" + entity.roles())));
        }


        return user;
    }
}

