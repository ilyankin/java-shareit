package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingItemDtoOut;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDtoIn> itemDtoInJson;

    @Autowired
    private JacksonTester<ItemDtoOut> itemDtoOutJson;


    @Test
    void testItemDtoIn() throws IOException {
        var itemDtoIn = new ItemDtoIn();
        itemDtoIn.setName("ItemName");
        itemDtoIn.setDescription("ItemDescription");
        itemDtoIn.setAvailable(false);
        itemDtoIn.setRequestId(1L);

        var result = itemDtoInJson.write(itemDtoIn);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("ItemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void testItemDtoOut() throws IOException {
        var itemDtoOut = new ItemDtoOut();
        itemDtoOut.setId(1L);
        itemDtoOut.setName("ItemName");
        itemDtoOut.setDescription("ItemDescription");
        itemDtoOut.setAvailable(false);
        itemDtoOut.setRequestId(1L);
        itemDtoOut.setLastBooking(new BookingItemDtoOut());
        itemDtoOut.setNextBooking(new BookingItemDtoOut());
        itemDtoOut.setComments(List.of(new CommentDtoOut()));

        var result = itemDtoOutJson.write(itemDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("ItemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.lastBooking", BookingItemDtoOut.class).isNotNull();
        assertThat(result).extractingJsonPathValue("$.nextBooking", BookingItemDtoOut.class).isNotNull();
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(1);
    }
}
