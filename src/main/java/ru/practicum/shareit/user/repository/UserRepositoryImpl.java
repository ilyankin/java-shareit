package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserEmailAlreadyTakenException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static long userCounter = 0;

    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        checkAvailabilityEmail(user);
        user.setId(generateId());
        var id = user.getId();
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User update(User user) {
        String email = user.getEmail();
        User userFromDb = findByEmail(email).orElse(null);
        Long id = user.getId();
        if (userFromDb != null) {
            if (user.getEmail() != null && userFromDb.getId().equals(id)) {
                users.put(id, user);
                return users.get(id);
            } else throw new UserEmailAlreadyTakenException(email);
        }
        users.put(id, user);
        return users.get(id);
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
        return ++userCounter;
    }

    private void checkAvailabilityEmail(User user) {
        String email = user.getEmail();
        findByEmail(email).ifPresent((u) -> {
            throw new UserEmailAlreadyTakenException(email);
        });
    }
}
