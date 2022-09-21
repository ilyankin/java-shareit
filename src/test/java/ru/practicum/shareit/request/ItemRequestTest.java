package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ItemRequestTest {
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private final long REQUESTER_ID = 1;
    private final long ITEM_REQUEST_ID = 1;

    private User requester;
    private ItemRequest itemRequest;
    private ItemRequestDtoIn itemRequestDtoIn;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final LocalDateTime NOW = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER));


    @BeforeEach
    void setUp() {
        requester = new User();
        requester.setId(REQUESTER_ID);
        requester.setName("RequesterName");
        requester.setEmail("RequesterEmail");

        itemRequest = new ItemRequest();
        itemRequest.setId(ITEM_REQUEST_ID);
        itemRequest.setRequester(requester);
        itemRequest.setDescription("ItemRequestDescription");
        itemRequest.setCreated(NOW);
        itemRequest.setItems(List.of(new Item()));

        itemRequestDtoIn = new ItemRequestDtoIn();
        itemRequestDtoIn.setDescription(itemRequest.getDescription());
    }

    @Test
    void saveItemRequest() {
        Mockito.when(userService.getUserById(REQUESTER_ID))
                .thenReturn(UserMapper.toUserDto(requester));
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequest);

        var itemRequestDtoOut = itemRequestService.saveItemRequest(REQUESTER_ID, itemRequestDtoIn);

        assertThat(itemRequestDtoOut.getDescription(), is(itemRequestDtoIn.getDescription()));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .save(Mockito.any(ItemRequest.class));
    }

    @Test
    void getItemRequestById() {
        Mockito.when(userService.getUserById(REQUESTER_ID))
                .thenReturn(UserMapper.toUserDto(requester));
        Mockito.when(itemRequestRepository.findById(ITEM_REQUEST_ID))
                .thenReturn(Optional.of(itemRequest));

        var itemRequestDtoOut = itemRequestService.getItemRequestById(REQUESTER_ID, ITEM_REQUEST_ID);

        assertThat(itemRequestDtoOut.getId(), is(itemRequest.getId()));
        assertThat(itemRequestDtoOut.getDescription(), is(itemRequest.getDescription()));
        assertThat(itemRequestDtoOut.getCreated(), is(itemRequest.getCreated()));
        assertThat(itemRequestDtoOut.getItems().size(), is(1));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findById(ITEM_REQUEST_ID);
    }

    @Test
    void getItemRequestsByRequesterId() {
        Mockito.when(userService.getUserById(REQUESTER_ID))
                .thenReturn(UserMapper.toUserDto(requester));
        Mockito.when(itemRequestRepository.findAllByRequesterId(eq(REQUESTER_ID), Mockito.any(Sort.class)))
                .thenReturn(List.of(itemRequest));

        var itemRequests = itemRequestService.getItemRequestsByRequesterId(REQUESTER_ID);

        assertThat(itemRequests.size(), is(1));

        var itemRequestDtoOut = itemRequests.get(0);

        assertThat(itemRequestDtoOut.getId(), is(itemRequest.getId()));
        assertThat(itemRequestDtoOut.getDescription(), is(itemRequest.getDescription()));
        assertThat(itemRequestDtoOut.getCreated(), is(itemRequest.getCreated()));
        assertThat(itemRequestDtoOut.getItems().size(), is(1));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findAllByRequesterId(eq(REQUESTER_ID), Mockito.any(Sort.class));
    }

    @Test
    void getItemRequestsFromOtherUsers() {
        Mockito.when(userService.getUserById(REQUESTER_ID))
                .thenReturn(UserMapper.toUserDto(requester));
        Mockito.when(itemRequestRepository.findAllByRequesterIdNot(eq(REQUESTER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(itemRequest));

        var itemRequests = itemRequestService.getItemRequestsFromOtherUsers(REQUESTER_ID, 1, 10);

        assertThat(itemRequests.size(), is(1));

        var itemRequestDtoOut = itemRequests.get(0);

        assertThat(itemRequestDtoOut.getId(), is(itemRequest.getId()));
        assertThat(itemRequestDtoOut.getDescription(), is(itemRequest.getDescription()));
        assertThat(itemRequestDtoOut.getCreated(), is(itemRequest.getCreated()));
        assertThat(itemRequestDtoOut.getItems().size(), is(1));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findAllByRequesterIdNot(eq(REQUESTER_ID), Mockito.any(Pageable.class));
    }
}
