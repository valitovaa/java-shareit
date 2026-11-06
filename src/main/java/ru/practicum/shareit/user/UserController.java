package ru.practicum.shareit.user;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dal.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<User> userList = userService.findAll();

        List<UserDto> userDtoList = userList.stream()
                .map(UserMapper::map)
                .toList();

        return ResponseEntity.ok(userDtoList);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto){
        User user = UserMapper.map(userDto);
        User adddedUser = userService.addUser(user);
        return ResponseEntity.ok(UserMapper.map(adddedUser));

    }
    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto){
        User user = UserMapper.map(userDto);
        User updatedUser = userService.update(user);
        return  ResponseEntity.ok(UserMapper.map(updatedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable @Positive Long id){
        User user = userService.findUserById(id);
        return ResponseEntity.ok(UserMapper.map(user));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive Long userId) {
        userService.deleteUser(userId);
    }
}
