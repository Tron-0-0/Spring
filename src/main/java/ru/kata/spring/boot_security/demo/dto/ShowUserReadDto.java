package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;

public record ShowUserReadDto(Long id,
                              String username,
                              Byte age,
                              String firstname,
                              String lastname,
                              List<Role> roles) {
}
