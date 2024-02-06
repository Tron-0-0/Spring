package ru.kata.spring.boot_security.demo.dto;

public record ShowUserReadDto(Long id,
                              String username,
                              Byte age,
                              String firstname,
                              String lastname,
                              String roles) {
}
