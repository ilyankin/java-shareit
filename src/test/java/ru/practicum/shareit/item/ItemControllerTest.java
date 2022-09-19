package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final long USER_ID_HEADER_VALUE = 1;
    private static final long ITEM_ID = 1;
    private ItemDtoOut itemDtoOut;
    private ItemDtoIn itemDtoIn;

    @BeforeEach
    void setUp() {
        itemDtoIn = new ItemDtoIn();
        itemDtoIn.setName("ItemInName");
        itemDtoIn.setDescription("ItemInDescription");
        itemDtoIn.setAvailable(false);

        itemDtoOut = new ItemDtoOut();
        itemDtoOut.setName("ItemOutName");
        itemDtoOut.setDescription("ItemOutDescription");
        itemDtoOut.setAvailable(true);
    }

    @Test
    @DisplayName("successfullyGetOneItemById [200]")
    void getItemById() throws Exception {
        Mockito.when(itemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDtoOut);

        mvc.perform(get("/items/{itemId}", ITEM_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable()), Boolean.class));
    }

    @Test
    @DisplayName("successfullyGetItems [200]")
    void getItems() throws Exception {
        Mockito.when(itemService.getItems(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemDtoOut));

        mvc.perform(get("/items", ITEM_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoOut.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemDtoOut.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemDtoOut.getAvailable()), Boolean.class));
    }

    @Test
    @DisplayName("successfullySaveItem [200]")
    void saveItem() throws Exception {
        Mockito.when(itemService.saveItem(Mockito.any(ItemDtoIn.class), Mockito.anyLong()))
                .thenReturn(itemDtoOut);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable()), Boolean.class));
    }

    @Test
    @DisplayName("successfullyUpdateItem [200]")
    void updateItem() throws Exception {
        itemDtoOut.setName(itemDtoIn.getName());
        itemDtoOut.setDescription(itemDtoIn.getDescription());
        itemDtoOut.setAvailable(itemDtoIn.getAvailable());

        Mockito.when(itemService.updateItem(eq(itemDtoIn), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDtoOut);

        mvc.perform(patch("/items/{itemId}", ITEM_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDtoOut.getAvailable()), Boolean.class));
    }

    @Test
    @DisplayName("successfullySearchItem [200]")
    void searchItem() throws Exception {
        Mockito.when(itemService.searchItem(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(itemDtoOut));

        mvc.perform(get("/items/search?text='test'")
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoOut.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemDtoOut.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemDtoOut.getAvailable()), Boolean.class));
    }

    @Test
    @DisplayName("successfullySaveCommentForItem [200]")
    void saveComment() throws Exception {
        var commentDtoIn = new CommentDtoIn();
        commentDtoIn.setText("text");

        var commentDtoOut = new CommentDtoOut();
        commentDtoOut.setId(1L);
        commentDtoOut.setText(commentDtoIn.getText());
        commentDtoOut.setItemName("ItemName");
        commentDtoOut.setAuthorName("AuthorName");

        Mockito.when(itemService.saveComment(eq(commentDtoIn), Mockito.any(), Mockito.any()))
                .thenReturn(commentDtoOut);

        mvc.perform(post("/items/{itemId}/comment", ITEM_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDtoOut.getText()), String.class))
                .andExpect(jsonPath("$.itemName", is(commentDtoOut.getItemName()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentDtoOut.getAuthorName()), String.class));
    }
}