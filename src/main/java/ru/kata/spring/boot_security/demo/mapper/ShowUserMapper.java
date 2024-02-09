package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.beans.factory.Aware;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.ShowUserReadDto;
import ru.kata.spring.boot_security.demo.entity.User;

@Component
public class ShowUserMapper implements Mapper<ShowUserReadDto, User> {
    @Override
    public ShowUserReadDto map(User entity) {
        return new ShowUserReadDto(
                entity.getId(),
                entity.getUsername(),
                entity.getAge(),
                entity.getFirstname(),
                entity.getLastname(),
                entity.getRoles()
        );
    }
}
