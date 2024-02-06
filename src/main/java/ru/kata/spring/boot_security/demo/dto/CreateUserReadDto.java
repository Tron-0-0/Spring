package ru.kata.spring.boot_security.demo.dto;

public record CreateUserReadDto(Long id,
                                String username,
                                Byte age,
                                String firstname,
                                String lastname,
                                String password,
                                String roles) {
    public static CreateUserReadDto emptyObject() {
        return new CreateUserReadDto(null, null, null, null, null, null, null);
    }
}
