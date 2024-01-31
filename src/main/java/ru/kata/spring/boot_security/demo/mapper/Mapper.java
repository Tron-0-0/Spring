package ru.kata.spring.boot_security.demo.mapper;

public interface Mapper <TO, FROM> {

    TO map(FROM entity);

    default FROM reverse(TO entity) {
        return null;
    }
}
