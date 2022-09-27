package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.user.UserNotFoundByIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long userId) {
        log.debug("The request to getUserById(userId={})", userId);
        return UserMapper.toUserDto(findByIdOrElseThrow(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("The request to getAllUsers()");
        return UserMapper.toUserDtos(userRepository.findAll());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        log.debug("The request to saveUser(userDto={})", userDto);
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.debug("The request to updateUser(userId={}, userDto={})", userId, userDto);
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
        log.debug("The request to deleteUser(userId={})", userId);
        findByIdOrElseThrow(userId);
        userRepository.deleteById(userId);
    }

    private User findByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundByIdException(userId));
    }
}
