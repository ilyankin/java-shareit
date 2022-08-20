package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
public class ItemRepositoryImp implements ItemRepository {
    private static long itemCounter = 0;
    private final HashMap<Long, Item> items = new HashMap<>();

    @Override
    public Item save(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAllByOwnerId(Long userId) {
        return items.values().stream()
                .filter(item -> userId.equals(item.getOwner().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchByText(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }

    private long generateId() {
        return ++itemCounter;
    }
}
