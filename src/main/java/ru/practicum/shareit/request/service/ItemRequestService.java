package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut saveItemRequest(Long requesterId, ItemRequestDtoIn itemRequestDto);

    ItemRequestDtoOut getItemRequestById(Long requesterId, Long requestId);

    List<ItemRequestDtoOut> getItemRequestsByRequesterId(Long requesterId);

    Iterable<ItemRequestDtoOut> getItemRequestsFromOtherUsers(Long requesterId, Integer from, Integer size);
}
