package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public record CreateUserReadDto(Long id,
                                String username,
                                Byte age,
                                String firstname,
                                String lastname,
                                String password,
                                List<String> roles) {

}
