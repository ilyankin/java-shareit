package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final long USER_ID_HEADER_VALUE = 1;
    private static final long ITEM_REQUEST_ID = 1;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final LocalDateTime NOW = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    private ItemRequestDtoOut itemRequestDtoOut;
    private ItemRequestDtoIn itemRequestDtoIn;


    @BeforeEach
    void setUp() {
        itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription("ItemRequestInDescription");

        itemRequestDtoOut = new ItemRequestDtoOut();
        itemRequestDtoOut.setId(ITEM_REQUEST_ID);
        itemRequestDtoOut.setDescription("ItemRequestOutDescription");
        itemRequestDtoOut.setCreated(NOW);
    }

    @Test
    @DisplayName("successfullySaveItemRequest [200]")
    void saveItemRequest() throws Exception {
        Mockito.when(itemRequestService.saveItemRequest(Mockito.anyLong(), eq(itemRequestDtoIn)))
                .thenReturn(itemRequestDtoOut);

        mvc.perform(post("/requests")
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoOut.getDescription()), String.class));
    }

    @Test
    @DisplayName("successfullyGetOneItemRequestById [200]")
    void getItemRequestById() throws Exception {
        Mockito.when(itemRequestService.getItemRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemRequestDtoOut);

        mvc.perform(get("/requests/{requestId}", ITEM_REQUEST_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoOut.getDescription()), String.class));
    }

    @Test
    @DisplayName("successfullyGetAllItemRequestsByRequesterId [200]")
    void getAllItemRequestsByRequesterId() throws Exception {
        Mockito.when(itemRequestService.getItemRequestsByRequesterId(Mockito.anyLong()))
                .thenReturn(List.of(itemRequestDtoOut));

        mvc.perform(get("/requests", ITEM_REQUEST_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoOut.getDescription()), String.class));
    }

    @Test
    @DisplayName("successfullyGetAllItemRequestsFromOtherUsers[200]")
    void getAllItemRequestsFromOtherUsers() throws Exception {
        Mockito.when(itemRequestService.getItemRequestsFromOtherUsers(Mockito.anyLong(),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemRequestDtoOut));

        mvc.perform(get("/requests/all", ITEM_REQUEST_ID)
                        .header(USER_ID_HEADER, USER_ID_HEADER_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoOut.getDescription()), String.class));
    }
}