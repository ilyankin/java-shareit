package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOut saveItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemRequestDtoIn itemRequestDto) {
        return itemRequestService.saveItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long requestId) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDtoOut> getAllItemRequestsByRequesterId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestsByRequesterId(userId);
    }

    @GetMapping("/all")
    public Iterable<ItemRequestDtoOut> getAllItemRequestsFromOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0", required = false) Integer from,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemRequestService.getItemRequestsFromOtherUsers(userId, from, size);
    }
}
