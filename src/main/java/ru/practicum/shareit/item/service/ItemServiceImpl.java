package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.exception.UserIsNotOwnerException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public ItemDtoOut getItemById(Long itemId, Long userId) {
        var item = itemRepository.findById(itemId)
                        .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDtoOut saveItem(ItemDtoIn itemDtoIn, Long userId) {
        var owner = userService.getUserById(userId);
        var item = itemMapper.toItem(itemDtoIn);
        item.setOwner(userMapper.toUser(owner));
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDtoOut updateItem(ItemDtoIn itemDtoIn, Long itemId, Long userId) {
        var owner = userService.getUserById(userId);
        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        var ownerId = owner.getId();
        if (!ownerId.equals(item.getOwner().getId())) throw new UserIsNotOwnerException(ownerId, itemId);

        var updatedItem = itemMapper.toItem(itemDtoIn);
        var updatedItemName = updatedItem.getName();
        var updatedItemDescription = updatedItem.getDescription();
        var updatedItemAvailable = updatedItem.getAvailable();

        if (updatedItemName != null && !updatedItemName.isBlank()) item.setName(updatedItemName);
        if (updatedItemDescription != null && !updatedItemDescription.isBlank())
            item.setDescription(updatedItemDescription);
        if (updatedItemAvailable != null) item.setAvailable(updatedItemAvailable);

        return itemMapper.toItemDto(itemRepository.update(item));
    }

    @Override
    public List<ItemDtoOut> getAllItems(Long userId) {
        return itemMapper.toItemDtos(itemRepository.findAllByOwnerId(userId));
    }

    @Override
    public List<ItemDtoOut> searchItem(String text) {
        return itemMapper.toItemDtos(itemRepository.searchByText(text.toLowerCase().trim()));
    }
}
