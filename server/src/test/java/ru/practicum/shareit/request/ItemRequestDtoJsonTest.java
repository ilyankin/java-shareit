package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDtoIn> itemRequestDtoInJson;

    @Autowired
    private JacksonTester<ItemRequestDtoOut> itemRequestDtoOutJson;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_WITH_SECONDS
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final LocalDateTime NOW = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER));

    @Test
    void testItemRequestDtoIn() throws IOException {
        var itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription("ItemRequestDescription");

        var result = itemRequestDtoInJson.write(itemRequestDtoIn);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("ItemRequestDescription");
    }

    @Test
    void testItemRequestDtoOut() throws IOException {
        var itemRequestDtoOut = new ItemRequestDtoOut();
        itemRequestDtoOut.setId(1L);
        itemRequestDtoOut.setDescription("ItemRequestDescription");
        itemRequestDtoOut.setCreated(NOW);
        itemRequestDtoOut.setItems(List.of(new ItemDtoOut()));

        var result = itemRequestDtoOutJson.write(itemRequestDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("ItemRequestDescription");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(NOW.format(DATE_TIME_FORMATTER_WITH_SECONDS));
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
    }
}
