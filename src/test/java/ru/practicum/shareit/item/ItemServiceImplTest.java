package ru.practicum.shareit.item;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDtoIn;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
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
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private ItemServiceImpl itemService;

    private final long USER_ID = 1;
    private final long REQUESTER_ID = 2;
    private final long ITEM_ID = 1;
    private final long COMMENT_ID = 1;
    private final long ITEM_REQUEST_ID = 1;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final LocalDateTime NOW = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER));

    private User user;
    private User requester;
    private Item item;
    private Comment comment;
    private Booking booking;
    private ItemRequest itemRequest;
    private ItemDtoIn itemDtoIn;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setName("UserName");
        user.setEmail("UserEmail");

        requester = new User();
        requester.setId(REQUESTER_ID);
        requester.setName("RequesterName");
        requester.setEmail("RequesterEmail");

        itemRequest = new ItemRequest();
        itemRequest.setId(ITEM_REQUEST_ID);
        itemRequest.setRequester(requester);
        itemRequest.setDescription("ItemRequestDescription");
        itemRequest.setCreated(NOW);

        item = new Item();
        item.setId(ITEM_ID);
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(itemRequest);

        comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setText("CommentName");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(NOW.plusMinutes(1));

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(requester);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(NOW.plusMinutes(1));
        booking.setEnd(NOW.plusDays(1));

        itemDtoIn = new ItemDtoIn();
        itemDtoIn.setName("name");
        itemDtoIn.setDescription("name");
        itemDtoIn.setAvailable(true);
        itemDtoIn.setRequestId(REQUESTER_ID);
    }

    @Test
    void getItemById() {
        Mockito.when(userService.getUserById(USER_ID))
                .thenReturn(UserMapper.toUserDto(user));
        Mockito.when(itemRepository.findById(ITEM_ID))
                .thenReturn(Optional.of(item));
        Mockito.when(commentRepository.findCommentsByItemId(COMMENT_ID))
                .thenReturn(List.of(comment));
        Mockito.when(bookingRepository.findLastBookingByItemId(ITEM_ID))
                .thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.findNextBookingByItemId(ITEM_ID))
                .thenReturn(Optional.of(booking));

        var itemDto = itemService.getItemById(ITEM_ID, USER_ID);

        assertThat(itemDto.getId(), is(item.getId()));
        assertThat(itemDto.getName(), is(item.getName()));
        assertThat(itemDto.getDescription(), is(item.getDescription()));
        assertThat(itemDto.getAvailable(), is(item.getAvailable()));
        assertThat(itemDto.getComments().size(), is(1));
        assertThat(itemDto.getLastBooking().getId(), is(booking.getId()));
        assertThat(itemDto.getNextBooking().getId(), is(booking.getId()));

        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(ITEM_ID);
    }


    @Test
    void getAllItemsByUser() {
        Mockito.when(userService.getUserById(USER_ID))
                .thenReturn(UserMapper.toUserDto(user));
        Mockito.when(itemRepository.findAllByOwnerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(item));
        Mockito.when(commentRepository.findCommentsByItemId(ITEM_ID))
                .thenReturn(List.of(comment));
        Mockito.when(bookingRepository.findLastBookingByItemId(ITEM_ID))
                .thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.findNextBookingByItemId(ITEM_ID))
                .thenReturn(Optional.of(booking));

        var items = itemService.getItems(USER_ID, 1, 10);

        assertThat(items.size(), is(1));

        var itemDto = items.get(0);
        assertThat(itemDto.getId(), is(item.getId()));
        assertThat(itemDto.getName(), is(item.getName()));
        assertThat(itemDto.getDescription(), is(item.getDescription()));
        assertThat(itemDto.getAvailable(), is(item.getAvailable()));
        assertThat(itemDto.getComments().get(0).getText(), is(comment.getText()));
        assertThat(itemDto.getLastBooking().getId(), is(booking.getId()));
        assertThat(itemDto.getNextBooking().getId(), is(booking.getId()));

        Mockito.verify(itemRepository, Mockito.times(1))
                .findAllByOwnerId(eq(ITEM_ID), Mockito.any(Pageable.class));
    }


    @Test
    void saveItem() {
        Mockito.when(userService.getUserById(USER_ID))
                .thenReturn(UserMapper.toUserDto(user));
        Mockito.when(itemRequestRepository.findById(REQUESTER_ID))
                .thenReturn(Optional.of(itemRequest));
        Mockito.when(itemRepository.save(Mockito.any()))
                .thenReturn(item);


        var itemDto = itemService.saveItem(itemDtoIn, ITEM_ID);

        assertThat(itemDto.getId(), is(item.getId()));
        assertThat(itemDto.getName(), is(item.getName()));
        assertThat(itemDto.getDescription(), is(item.getDescription()));
        assertThat(itemDto.getAvailable(), is(item.getAvailable()));

        Mockito.verify(itemRepository, Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void updateItem() {
        Mockito.when(userService.getUserById(USER_ID))
                .thenReturn(UserMapper.toUserDto(user));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(Mockito.any()))
                .thenReturn(item);

        var itemDtoOut = itemService.updateItem(itemDtoIn, ITEM_ID, USER_ID);

        assertThat(itemDtoOut.getId(), is(ITEM_ID));
        assertThat(itemDtoOut.getName(), is(itemDtoIn.getName()));
        assertThat(itemDtoOut.getDescription(), is(itemDtoIn.getDescription()));
        assertThat(itemDtoOut.getAvailable(), is(itemDtoIn.getAvailable()));

        Mockito.verify(itemRepository, Mockito.times(1))
                .save(item);
    }

    @Test
    void saveComment() {
        Mockito.when(userService.getUserById(REQUESTER_ID))
                .thenReturn(UserMapper.toUserDto(requester));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findPastBookingsByBookerId(eq(REQUESTER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        var commentDtoIn = new CommentDtoIn();
        commentDtoIn.setText(comment.getText());

        var commentDtoOut = itemService.saveComment(commentDtoIn, REQUESTER_ID, ITEM_ID);

        assertThat(commentDtoOut.getText(), is(commentDtoIn.getText()));

        Mockito.verify(commentRepository, Mockito.times(1))
                .save(Mockito.any(Comment.class));
    }
}
