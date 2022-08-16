package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ItemRepositoryImp implements ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();

    @Override
    public Item save(Item item) {
        item.setId(generateId());
        return items.put(item.getId(), item);
    }

    @Override
    public Item update(Item item) {
        findById(item.getId()).orElseThrow(() -> new IllegalArgumentException("Item с таким id не существует"));
        return items.put(item.getId(), item);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }

    private long generateId() {
        return items.values().stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0) + 1;
    }
}
