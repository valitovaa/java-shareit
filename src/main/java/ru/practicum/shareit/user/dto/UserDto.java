package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    Long id;
    String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    String email;
}
