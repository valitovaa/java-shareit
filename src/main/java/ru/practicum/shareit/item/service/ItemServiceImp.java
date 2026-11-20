package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dal.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    @Override
    @Transactional
    public Item updateItem(Long itemId, ItemDto itemDto, Long userId) {

        Item item = findItemById(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Нет прав на редактирование");
        }

        if (itemDto.getName() != null) {
            if (itemDto.getName().isBlank()) {
                throw new ConditionsNotMetException(
                        "Название вещи не может быть пустым"
                );
            }
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            if (itemDto.getDescription().isBlank()) {
                throw new ConditionsNotMetException(
                        "Описание вещи не может быть пустым"
                );
            }
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return itemRepository.save(item);
    }

    @Override
    public List<OwnerItemDto> findAllByOwnerId(Long userId) {

        LocalDateTime now = LocalDateTime.now();

        List<Item> items = itemRepository.findAllByOwnerId(userId);

        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        List<Booking> pastBookings =
                bookingRepository.findAllByItem_IdInAndStatusAndEndBeforeOrderByStartDesc(
                        itemIds, BookingStatus.APPROVED, now
                );

        List<Booking> futureBookings =
                bookingRepository.findAllByItem_IdInAndStatusAndStartAfterOrderByStartAsc(
                        itemIds, BookingStatus.APPROVED, now
                );

        Map<Long, Booking> lastBookings = new HashMap<>();

        for (Booking booking : pastBookings) {
            lastBookings.putIfAbsent(
                    booking.getItem().getId(), booking
            );
        }

        Map<Long, Booking> nextBookings = new HashMap<>();

        for (Booking booking : futureBookings) {
            nextBookings.putIfAbsent(
                    booking.getItem().getId(), booking
            );
        }

        return items.stream()
                .map(item -> new OwnerItemDto(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        BookingMapper.mapToInfo(lastBookings.get(item.getId())),
                        BookingMapper.mapToInfo(nextBookings.get(item.getId())),
                        findCommentsByItemId(item.getId())
                ))
                .toList();
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.search(text);
    }

    @Override
    public List<CommentDto> findCommentsByItemId(Long itemId) {
        return commentRepository
                .findAllByItem_IdOrderByCreatedDesc(itemId)
                .stream()
                .map(CommentMapper::map)
                .toList();
    }

    @Override
    @Transactional
    public Comment postComment(Long itemId, Long userId, String text) {

        LocalDateTime now = LocalDateTime.now();

        boolean hasCompletedBooking =
                bookingRepository.existsByBookerIdAndItem_IdAndStatusAndEndBefore(
                        userId,
                        itemId,
                        BookingStatus.APPROVED,
                        now
                );

        if (!hasCompletedBooking) {
            throw new ConditionsNotMetException(
                    "Оставить комментарий можно только после завершения аренды"
            );
        }

        Item item = findItemById(itemId);
        User author = userService.findUserById(userId);

        Comment comment = new Comment();
        comment.setText(text);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(now);

        return commentRepository.save(comment);
    }
}