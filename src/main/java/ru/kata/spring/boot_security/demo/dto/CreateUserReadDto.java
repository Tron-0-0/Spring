package ru.kata.spring.boot_security.demo.dto;

public record CreateUserReadDto(String username,
                                String firstname,
                                String lastname,
                                String birthdate,
                                String password,
                                String role) {
}
