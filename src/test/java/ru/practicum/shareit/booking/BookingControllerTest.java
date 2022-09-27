package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final long USER_ID_HEADER_VALUE = 1;
    private static final long BOOKING_ID = 1;
    private static final long ITEM_ID = 1;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final LocalDateTime NOW = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    private BookingDtoOut bookingDtoOut;

    @BeforeEach
    void setUp() {
        var item = new Item();
        item.setId(ITEM_ID);

        bookingDtoOut = new BookingDtoOut();
        bookingDtoOut.setId(BOOKING_ID);
        bookingDtoOut.setItem(item);
        bookingDtoOut.setStatus(BookingStatus.WAITING);
    }


    @Test
    @DisplayName("successfullyGetOneBookingById [200]")
    void getBookingById() throws Exception {
        Mockito.when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDtoOut);

        mvc.perform(get("/bookings/{bookingId}", BOOKING_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoOut.getStart()), LocalDateTime.class))
                .andExpect(jsonPath("$.end", is(bookingDtoOut.getEnd()), LocalDateTime.class))
                .andExpect(jsonPath("$.item.id", is(bookingDtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker", is(bookingDtoOut.getBooker()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoOut.getStatus().name()), String.class));

    }

    @Test
    @DisplayName("successfullyGetBookingsByBookerIdWithAnyState [200]")
    void getAllBookingsByBookerId() throws Exception {
        Mockito.when(bookingService.getBookingsByBookerId(Mockito.anyLong(), Mockito.anyString(),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingDtoOut));

        mvc.perform(get("/bookings?state=ALL", BOOKING_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDtoOut.getStart()), LocalDateTime.class))
                .andExpect(jsonPath("$[0].end", is(bookingDtoOut.getEnd()), LocalDateTime.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker", is(bookingDtoOut.getBooker()), User.class))
                .andExpect(jsonPath("$[0].status", is(bookingDtoOut.getStatus().name()), String.class));
    }

    @Test
    @DisplayName("successfullyGetBookingsByItemsOwnerIdWithAnyState [200]")
    void getAllBookingsByItemsOwnerId() throws Exception {
        Mockito.when(bookingService.getBookingsByItemOwnerId(Mockito.anyLong(), Mockito.anyString(),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingDtoOut));

        mvc.perform(get("/bookings/owner?state=ALL", BOOKING_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDtoOut.getStart()), LocalDateTime.class))
                .andExpect(jsonPath("$[0].end", is(bookingDtoOut.getEnd()), LocalDateTime.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker", is(bookingDtoOut.getBooker()), User.class))
                .andExpect(jsonPath("$[0].status", is(bookingDtoOut.getStatus().name()), String.class));
    }

    @Test
    @DisplayName("successfullySaveBooking [200]")
    void saveBooking() throws Exception {
        Mockito.when(bookingService.saveBooking(Mockito.any(BookingDtoIn.class), Mockito.anyLong()))
                .thenReturn(bookingDtoOut);

        var bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setStart(NOW.plusMinutes(1));
        bookingDtoIn.setEnd(NOW.plusHours(1));
        bookingDtoIn.setItemId(ITEM_ID);

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoOut.getStart()), LocalDateTime.class))
                .andExpect(jsonPath("$.end", is(bookingDtoOut.getEnd()), LocalDateTime.class))
                .andExpect(jsonPath("$.item.id", is(bookingDtoOut.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker", is(bookingDtoOut.getBooker()), User.class))
                .andExpect(jsonPath("$.status", is(bookingDtoOut.getStatus().name()), String.class));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("successfullyUpdateBookingStatus [200]")
    void updateBookingStatus(boolean approved) throws Exception {
        Mockito.when(bookingService.updateBookingStatus(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenAnswer(invocationOnMock -> {
                    if (invocationOnMock.getArgument(2, Boolean.class)) { // approved
                        bookingDtoOut.setStatus(BookingStatus.APPROVED);
                    } else {
                        bookingDtoOut.setStatus(BookingStatus.REJECTED);
                    }
                    return bookingDtoOut;
                });

        mvc.perform(patch("/bookings/{bookingId}?approved=" + approved, BOOKING_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(bookingDtoOut.getStatus().name()), String.class));
    }
}