package ru.practicum.shareit.user;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.user.UserNotFoundByIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDtoIn;

    private static final long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setName("UserName");
        user.setEmail("UserEmail");

        userDtoIn = new UserDto();
        userDtoIn.setId(user.getId());
        userDtoIn.setName(user.getName());
        userDtoIn.setEmail(user.getEmail());
    }

    @Test
    void getUserById() {
        Mockito.when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        var userDto = userService.getUserById(USER_ID);

        assertThat(userDto.getId(), is(user.getId()));
        assertThat(userDto.getName(), is(user.getName()));
        assertThat(userDto.getEmail(), is(user.getEmail()));

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(USER_ID);
    }

    @Test
    void getUserByWrongId() {
        final long wrongUserId = 100;

        final var userException = Assertions.assertThrows(
                UserNotFoundByIdException.class, () -> userService.getUserById(wrongUserId));

        Assertions.assertEquals(String.format("User with {id=%s} not found", wrongUserId),
                userException.getMessage());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(wrongUserId);
    }

    @Test
    void getAllUsers() {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user));

        var users = userService.getAllUsers();

        assertThat(users.size(), is(1));

        Mockito.verify(userRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void saveUser() {
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        var userDtoOut = userService.saveUser(userDtoIn);

        assertThat(userDtoOut.getId(), is(user.getId()));
        assertThat(userDtoOut.getName(), is(user.getName()));
        assertThat(userDtoOut.getEmail(), is(user.getEmail()));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(user);
    }

    @Test
    void updateUser() {
        Mockito.when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        var userDto = userService.updateUser(USER_ID, userDtoIn);

        assertThat(userDto.getId(), is(user.getId()));
        assertThat(userDto.getName(), is(user.getName()));
        assertThat(userDto.getEmail(), is(user.getEmail()));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(user);
    }

    @Test
    void deleteUser() {
        userRepository.deleteById(USER_ID);

        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(USER_ID);
    }
}
