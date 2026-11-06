package ru.practicum.shareit.user.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class User {

    Long id;
    String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    String email;

}
