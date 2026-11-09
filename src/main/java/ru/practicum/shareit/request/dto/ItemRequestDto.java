package ru.practicum.shareit.request.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    Long id;
    String description;
    Long requestorId;
    LocalDateTime created;
}
