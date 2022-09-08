package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.booking.BookingException;
import ru.practicum.shareit.exception.item.ItemNotFoundByIdException;
import ru.practicum.shareit.exception.user.UserIsNotOwnerException;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    public ItemDtoOut getItemById(Long itemId, Long userId) {
        userService.getUserById(userId);
        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundByIdException(itemId));


        ItemDtoOut itemDto = ItemMapper.toItemDto(item);
        if (item.getOwner().getId().equals(userId)) {
            setNextAndLastBookings(itemDto);
        }
        itemDto.setComments(CommentMapper.toCommentDtos(
                commentRepository.findCommentsByItemId(itemDto.getId())));
        return itemDto;
    }

    @Override
    public ItemDtoOut saveItem(ItemDtoIn itemDtoIn, Long userId) {
        var owner = userService.getUserById(userId);
        var item = ItemMapper.toItem(itemDtoIn);
        item.setOwner(UserMapper.toUser(owner));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDtoOut updateItem(ItemDtoIn itemDtoIn, Long itemId, Long userId) {
        var owner = userService.getUserById(userId);
        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundByIdException(itemId));

        var ownerId = owner.getId();
        if (!ownerId.equals(item.getOwner().getId())) throw new UserIsNotOwnerException(ownerId, itemId);

        var updatedItem = ItemMapper.toItem(itemDtoIn);
        var updatedItemName = updatedItem.getName();
        var updatedItemDescription = updatedItem.getDescription();
        var updatedItemAvailable = updatedItem.getAvailable();

        if (updatedItemName != null && !updatedItemName.isBlank()) item.setName(updatedItemName);
        if (updatedItemDescription != null && !updatedItemDescription.isBlank())
            item.setDescription(updatedItemDescription);
        if (updatedItemAvailable != null) item.setAvailable(updatedItemAvailable);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public List<ItemDtoOut> getAllItems(Long userId) {
        userService.getUserById(userId);
        var items = itemRepository.findAllByOwnerId(userId);
        var itemDtos = new ArrayList<ItemDtoOut>();

        for (Item item : items) {
            var itemDto = ItemMapper.toItemDto(item);
            if (item.getOwner().getId().equals(userId)) {
                setNextAndLastBookings(itemDto);
            }
            itemDto.setComments(CommentMapper.toCommentDtos(
                    commentRepository.findCommentsByItemId(itemDto.getId())));
            itemDtos.add(itemDto);
        }
        return itemDtos;
    }

    @Override
    public List<ItemDtoOut> searchItem(String text) {
        return ItemMapper.toItemDtos(itemRepository.searchByText(text.toLowerCase().trim()));
    }

    @Override
    public CommentDtoOut saveComment(CommentDtoIn commentDtoIn, Long userId, Long itemId) {
        var item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundByIdException(itemId));
        var user = UserMapper.toUser(userService.getUserById(userId));

        if (bookingRepository.findPastBookingsByBookerId(userId).isEmpty()) {
            throw new BookingException(String.format("The user {id=%s} don't book the item {id=%s}", userId, itemId));
        }

        Comment comment = CommentMapper.toComment(commentDtoIn);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDtoOut(commentRepository.save(comment));
    }

    private void setNextAndLastBookings(ItemDtoOut itemDto) {
        itemDto.setLastBooking(BookingMapper.toBookingItemDtoOut(
                bookingRepository.findLastBookingByItemId(itemDto.getId()).orElse(null)));
        itemDto.setNextBooking(BookingMapper.toBookingItemDtoOut(
                bookingRepository.findNextBookingByItemId(itemDto.getId()).orElse(null)));
    }
}
