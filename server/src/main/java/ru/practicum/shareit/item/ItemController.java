package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOut> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(defaultValue = "0", required = false) Integer from,
                                        @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemService.getItems(userId, from, size);
    }

    @PostMapping
    public ItemDtoOut saveItem(@RequestBody ItemDtoIn itemDtoIn,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.saveItem(itemDtoIn, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@RequestBody ItemDtoIn itemDtoIn,
                                 @PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(itemDtoIn, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchItem(@RequestParam String text,
                                       @RequestParam(defaultValue = "0", required = false) Integer from,
                                       @RequestParam(defaultValue = "10", required = false) Integer size) {
        return text == null || text.isBlank() ? Collections.emptyList() : itemService.searchItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId,
                                     @RequestBody CommentDtoIn commentDtoIn) {
        return itemService.saveComment(commentDtoIn, userId, itemId);
    }
}
