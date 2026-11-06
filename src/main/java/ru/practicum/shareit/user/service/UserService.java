package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public interface UserService {

    List<User> findAll();

    User addUser(User user);

    User update(Long userId, User newUser);

    User findUserById(Long id);

    void deleteUser(Long userId);
}
