package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dal.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    ItemService itemService;
    UserService userService;

    @PostMapping
    public ResponseEntity<ItemDto> addItem(
            @RequestBody @Valid  ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {

        User owner = userService.findUserById(userId);

        Item item = ItemMapper.map(itemDto);
        item.setOwner(owner);

        Item addeditem = itemService.addItem(item);
        return ResponseEntity.ok(ItemMapper.map(addeditem));

    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {

        Item item = itemService.updateItem(itemId, itemDto, userId);
        return ResponseEntity.ok(ItemMapper.map(item));

    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long itemId) {
        Item item = itemService.findItemById(itemId);
        return ResponseEntity.ok(ItemMapper.map(item));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAllByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {

        List<Item> itemList = itemService.findAllByOwnerId(userId);
        List<ItemDto> itemDtoList = convertListToDto(itemList);

        return ResponseEntity.ok(itemDtoList);

    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam String text) {

        List<Item> itemList = itemService.searchItems(text);
        List<ItemDto> itemDtoList = convertListToDto(itemList);

        return ResponseEntity.ok(itemDtoList);

    }

    public List<ItemDto> convertListToDto(List<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::map)
                .toList();
    }


}
