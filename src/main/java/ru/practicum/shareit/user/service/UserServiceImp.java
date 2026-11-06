package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    public final UserRepository userRepository;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConditionsNotMetException("Email уже используется");
        }
        return userRepository.addUser(user);
    }

    @Override
    public User update(Long userId, User newUser) {

        User oldUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!oldUser.getEmail().equalsIgnoreCase(newUser.getEmail())
                && userRepository.existsByEmail(newUser.getEmail())) {
            throw new ConditionsNotMetException("Email уже используется");
        }
        return userRepository.update(newUser);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
