package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.ShowUserReadDto;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.mapper.ShowUserMapper;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService, Aware {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;
    private final ShowUserMapper showUserMapper;

    @Autowired
    public UserService(UserRepository userRepository, ShowUserMapper showUserMapper) {
        this.userRepository = userRepository;
        this.showUserMapper = showUserMapper;
    }

    public ShowUserReadDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(showUserMapper::map)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!!!"));
    }

    public List<ShowUserReadDto> getAll() {
        return userRepository.getAllUsers().stream()
                .map(showUserMapper::map)
                .toList();
    }

    public boolean save(User user) {
        Optional<User> savesUsers = userRepository.findByUsername(user.getUsername());

        if (savesUsers.isPresent()) {
            return false;
        }

        userRepository.save(user);

        return true;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);

    }

    public void update(Long id, User mapEntity) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setUsername(mapEntity.getUsername());
            user.setFirstname(mapEntity.getFirstname());
            user.setLastname(mapEntity.getLastname());

            if (mapEntity.getPassword()!=null) {
                user.setPassword(mapEntity.getPassword());
            }
            user.setAge(mapEntity.getAge());

            user.getRoles().clear();
            user.getRoles().addAll(mapEntity.getRoles());

            entityManager.merge(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!!!"));

    }


}
