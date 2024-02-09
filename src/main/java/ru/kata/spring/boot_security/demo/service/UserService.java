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

    public ShowUserReadDto findById(Long id) {
        return userRepository.findById(id)
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

    public void update(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            User oldUser = optionalUser.get();

            oldUser.setUsername(user.getUsername());
            oldUser.setFirstname(user.getFirstname());
            oldUser.setLastname(user.getLastname());

            if (!user.getPassword().equals("undefined")) {
                oldUser.setPassword(user.getPassword());
            }

            oldUser.setAge(user.getAge());

            oldUser.getRoles().clear();
            oldUser.getRoles().addAll(user.getRoles());

            entityManager.merge(oldUser);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!!!"));

    }


}
