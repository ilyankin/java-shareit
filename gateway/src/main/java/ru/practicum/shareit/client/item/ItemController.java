package ru.practicum.shareit.client.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.comment.dto.CommentDtoIn;
import ru.practicum.shareit.client.item.dto.ItemDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                         @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemClient.getAllItems(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody ItemDtoIn itemDtoIn) {
        return itemClient.createItem(userId, itemDtoIn);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody ItemDtoIn itemDtoIn) {
        return itemClient.updateItem(userId, itemId, itemDtoIn);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId) {
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestParam String text,
                                               @PositiveOrZero
                                               @RequestParam(defaultValue = "0", required = false) Integer from,
                                               @Positive
                                               @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemClient.searchItemByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long itemId,
                                       @Valid @RequestBody CommentDtoIn commentDtoIn) {
        return itemClient.createComment(userId, itemId, commentDtoIn);
    }
}
