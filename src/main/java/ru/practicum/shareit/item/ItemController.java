package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOut> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                        @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemService.getAllItems(userId, from, size);
    }

    @PostMapping
    public ItemDtoOut saveItem(@Valid @RequestBody ItemDtoIn itemDtoIn,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.saveItem(itemDtoIn, userId);
    }

    @PatchMapping("/{itemId}")
    public @Valid ItemDtoOut updateItem(@RequestBody ItemDtoIn itemDtoIn,
                                        @PathVariable Long itemId,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(itemDtoIn, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchItem(@RequestParam String text,
                                       @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                       @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return text == null || text.isBlank() ? Collections.emptyList() : itemService.searchItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId,
                                     @Valid @RequestBody CommentDtoIn commentDtoIn) {
        return itemService.saveComment(commentDtoIn, userId, itemId);
    }
}
