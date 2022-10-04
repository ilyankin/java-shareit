package ru.practicum.shareit.client.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.booking.dto.BookingDtoIn;
import ru.practicum.shareit.client.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByBookerId(Long userId, String bookingState, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", bookingState,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByItemOwnerId(Long userId, String bookingState, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", bookingState,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingDtoIn bookingDto) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> updateBookingStatus(Long userId, Long bookingId, boolean approve) {
        Map<String, Object> parameters = Map.of("approved", Boolean.toString(approve));
        return patch("/" + bookingId + "?approved={approved}", userId, parameters);
    }
}
