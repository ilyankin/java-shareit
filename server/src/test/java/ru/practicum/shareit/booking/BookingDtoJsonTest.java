package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDtoIn> bookingDtoInJson;
    @Autowired
    private JacksonTester<BookingDtoOut> bookingDtoOutJson;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_WITH_SECONDS
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final LocalDateTime NOW = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER));

    @Test
    void testBookingDtoIn() throws IOException {
        var bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setItemId(1L);
        bookingDtoIn.setStart(NOW);
        bookingDtoIn.setEnd(NOW.plusMinutes(1));

        var result = bookingDtoInJson.write(bookingDtoIn);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(NOW.format(DATE_TIME_FORMATTER_WITH_SECONDS));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(NOW.plusMinutes(1)
                .format(DATE_TIME_FORMATTER_WITH_SECONDS));
    }

    @Test
    void testBookingDtoOut() throws IOException {
        var bookingDtoOut = new BookingDtoOut();
        bookingDtoOut.setId(1L);
        bookingDtoOut.setStatus(BookingStatus.WAITING);
        bookingDtoOut.setItem(new Item());
        bookingDtoOut.setBooker(new User());
        bookingDtoOut.setStart(NOW);
        bookingDtoOut.setEnd(NOW.plusMinutes(1));

        var result = bookingDtoOutJson.write(bookingDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(NOW.format(DATE_TIME_FORMATTER_WITH_SECONDS));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(NOW.plusMinutes(1)
                .format(DATE_TIME_FORMATTER_WITH_SECONDS));
        assertThat(result).extractingJsonPathValue("$.item", Item.class).isNotNull();
        assertThat(result).extractingJsonPathValue("$.booker", User.class).isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}
