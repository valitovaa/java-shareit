package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    User addUser(User user);

    User update(User newUser);

    Optional<User> findUserById(Long id);

    void deleteUser(Long userId);

}
