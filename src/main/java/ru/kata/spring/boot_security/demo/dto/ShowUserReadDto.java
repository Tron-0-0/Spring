package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.time.LocalDate;
import java.util.Set;

public record ShowUserReadDto(String username,
                              String firstname,
                              String lastname,
                              LocalDate birthdate,
                              Set<Role> roles)  {
}
