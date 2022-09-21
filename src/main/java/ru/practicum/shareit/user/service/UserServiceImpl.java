package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.user.UserNotFoundByIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(findByIdOrElseThrow(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toUserDtos(userRepository.findAll());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = findByIdOrElseThrow(userId);

        userDto.setId(userId);
        String email = userDto.getEmail();
        if (email == null || email.isBlank()) userDto.setEmail(user.getEmail());
        String name = userDto.getName();
        if (name == null || name.isBlank()) userDto.setName(user.getName());

        return UserMapper.toUserDto(userRepository.save((UserMapper.toUser(userDto))));
    }

    @Override
    public void deleteUser(Long userId) {
        findByIdOrElseThrow(userId);
        userRepository.deleteById(userId);
    }

    private User findByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundByIdException(userId));
    }
}
