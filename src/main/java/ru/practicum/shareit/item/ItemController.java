package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
    public ItemDto addItem(
            @RequestBody @Valid ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {

        User owner = userService.findUserById(userId);

        Item item = ItemMapper.map(itemDto);
        item.setOwner(owner);

        Item addeditem = itemService.addItem(item);
        return ItemMapper.map(addeditem);

    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {

        Item item = itemService.updateItem(itemId, itemDto, userId);
        return ItemMapper.map(item);

    }


    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        Item item = itemService.findItemById(itemId);
        return ItemMapper.map(item);
    }


    @GetMapping
    public List<ItemDto> findAllByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {

        List<Item> itemList = itemService.findAllByOwnerId(userId);

        return convertListToDto(itemList);

    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {

        List<Item> itemList = itemService.searchItems(text);

        return convertListToDto(itemList);

    }

    public List<ItemDto> convertListToDto(List<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::map)
                .toList();
    }


}
