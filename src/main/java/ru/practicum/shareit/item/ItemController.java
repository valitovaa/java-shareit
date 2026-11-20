package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dal.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public ItemDto addItem(
            @RequestBody @Valid ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        User owner = userService.findUserById(userId);

        Item item = ItemMapper.map(itemDto);
        item.setOwner(owner);

        Item addedItem = itemService.addItem(item);

        return mapToItemDto(addedItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        Item item = itemService.updateItem(itemId, itemDto, userId);

        return mapToItemDto(item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<OwnerItemDto> findAllByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return itemService.findAllByOwnerId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        List<Item> items = itemService.searchItems(text);

        return convertListToDto(items);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid CommentDto commentDto
    ) {
        return CommentMapper.map(
                itemService.postComment(itemId, userId, commentDto.getText())
        );
    }


    private ItemDto mapToItemDto(Item item) {
        List<CommentDto> comments =
                itemService.findCommentsByItemId(item.getId());

        return ItemMapper.map(item, comments, null, null);
    }

    private List<ItemDto> convertListToDto(List<Item> itemList) {
        return itemList.stream()
                .map(this::mapToItemDto)
                .toList();
    }

}