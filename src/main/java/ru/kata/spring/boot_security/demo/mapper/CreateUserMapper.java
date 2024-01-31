package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.CreateUserReadDto;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CreateUserMapper implements Mapper<User, CreateUserReadDto> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CreateUserMapper(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User map(CreateUserReadDto entity) {
        User user = new User();

        user.setUsername(entity.username());
        user.setFirstname(entity.firstname());
        user.setLastname(entity.lastname());
        user.setPassword(bCryptPasswordEncoder.encode(entity.password()));

        if (!entity.birthdate().isEmpty()) {
            user.setBirthdate(LocalDate.parse(entity.birthdate()));
        }

        if (entity.role().contains(",")) {
            user.setRoles(Arrays.stream(entity.role().split(","))
                    .map(Role::valueOf)
                    .collect(Collectors.toSet()));
        } else {
            user.setRoles(Collections.singleton(Role.valueOf(entity.role())));
        }


        return user;
    }
}
