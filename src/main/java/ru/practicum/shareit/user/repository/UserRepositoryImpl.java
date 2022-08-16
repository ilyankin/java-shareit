package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        user.setId(generateId());
        return users.put(user.getId(), user);
    }

    @Override
    public User update(User user) {
        findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User с таким id не существует"));
        return users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    private long generateId() {
        return users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0) + 1;
    }
}
