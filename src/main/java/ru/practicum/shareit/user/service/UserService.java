package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User addUser(User user);

    User update(User newUser);

    User findUserById(Long id);

    void deleteUser(Long userId);
}
