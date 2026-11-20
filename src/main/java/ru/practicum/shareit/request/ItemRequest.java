package ru.practicum.shareit.request;


import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    User requestor;

    @Column(name = "created")
    LocalDateTime created;
}
