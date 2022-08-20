package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAlreadyTakenException;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toUserDtos(userRepository.findAll());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        userDto.setId(userId);
        String email = userDto.getEmail();
        if (email == null || email.isBlank()) userDto.setEmail(user.getEmail());
        String name = userDto.getName();
        if (name == null || name.isBlank()) userDto.setName(user.getName());

        return userMapper.toUserDto(userRepository.update((userMapper.toUser(userDto))));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityAlreadyTakenException("User", "id", userId));
        userRepository.deleteById(userId);
    }
}
