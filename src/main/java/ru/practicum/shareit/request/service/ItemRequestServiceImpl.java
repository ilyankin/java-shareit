package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.request.ItemRequestNotFoundByIdException;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    public ItemRequestDtoOut saveItemRequest(Long requesterId, ItemRequestDtoIn itemRequestDto) {
        var requester = UserMapper.toUser(userService.getUserById(requesterId));
        var itemRequester = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequester.setRequester(requester);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequester));
    }

    @Override
    public ItemRequestDtoOut getItemRequestById(Long requesterId, Long itemRequestId) {
        userService.getUserById(requesterId);
        var itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new ItemRequestNotFoundByIdException(itemRequestId));
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDtoOut> getItemRequestsByRequesterId(Long requesterId) {
        userService.getUserById(requesterId);
        return ItemRequestMapper.toItemRequestDtos(itemRequestRepository.findAllByRequesterId(requesterId,
                Sort.by(Sort.Direction.DESC, "created")));
    }

    @Override
    public List<ItemRequestDtoOut> getItemRequestsFromOtherUsers(Long requesterId, Integer from, Integer size) {
        return ItemRequestMapper.toItemRequestDtos(itemRequestRepository.findAllByRequesterIdNot(requesterId,
                PageRequest.of(from, size, Sort.by("created").descending())));
    }
}
