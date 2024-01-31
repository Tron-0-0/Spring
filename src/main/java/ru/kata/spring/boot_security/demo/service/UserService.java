package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.User;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!!!");
        }

        return user.get();
    }

    public List<User> getAll() {
        return userRepository.getAllUsers();
    }

    public boolean save(User user) {
        Optional<User> savesUsers = userRepository.findByUsername(user.getUsername());

        if (savesUsers.isPresent()) {
            return false;
        }

        userRepository.save(user);

        return true;
    }

    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);

    }

    public void update(User map) {
        Optional<User> optionalUser = userRepository.findByUsername(map.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setUsername(map.getUsername());
            user.setFirstname(map.getFirstname());
            user.setLastname(map.getLastname());
            user.setPassword(map.getPassword());
            user.setBirthdate(map.getBirthdate());

            user.getRoles().clear();
            user.getRoles().addAll(map.getRoles());

            entityManager.merge(user);
        }
    }
}
