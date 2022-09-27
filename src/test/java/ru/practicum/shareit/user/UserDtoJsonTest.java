package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> userDtoJson;

    @Test
    void testUserDto() throws IOException {
        var userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("UserName");
        userDto.setEmail("userEmail@mail.com");

        var result = userDtoJson.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("UserName");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("userEmail@mail.com");
    }

}
