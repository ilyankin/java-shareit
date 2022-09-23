package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager tem;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void findAllByRequesterId() {
        var requester1 = new User();
        requester1.setEmail("user1@mail.com");
        requester1.setName("User1Name");

        var requester2 = new User();
        requester2.setEmail("user2@mail.com");
        requester2.setName("User2Name");

        var item = new Item();
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        item.setAvailable(true);
        item.setOwner(requester1);

        var itemRequest1 = new ItemRequest();
        itemRequest1.setRequester(requester1);
        itemRequest1.setDescription("ItemRequest1Description");
        itemRequest1.setCreated(LocalDateTime.now());

        var itemRequest2 = new ItemRequest();
        itemRequest2.setRequester(requester1);
        itemRequest2.setDescription("ItemRequest2Description");
        itemRequest2.setCreated(LocalDateTime.now());

        var itemRequest3 = new ItemRequest();
        itemRequest3.setRequester(requester2);
        itemRequest3.setDescription("ItemRequest3Description");
        itemRequest3.setCreated(LocalDateTime.now());

        tem.persist(requester1);
        tem.persist(requester2);
        tem.persist(item);
        tem.persist(itemRequest1);
        tem.persist(itemRequest2);
        tem.persist(itemRequest3);

        var itemRequests = itemRequestRepository.findAllByRequesterId(1L, Sort.unsorted());
        assertThat(itemRequests.size(), is(2));
    }

    @Test
    void findAllByRequesterIdNot() {
        var requester1 = new User();
        requester1.setEmail("user1@mail.com");
        requester1.setName("User1Name");

        var requester2 = new User();
        requester2.setEmail("user2@mail.com");
        requester2.setName("User2Name");

        var item = new Item();
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        item.setAvailable(true);
        item.setOwner(requester1);

        var itemRequest1 = new ItemRequest();
        itemRequest1.setRequester(requester1);
        itemRequest1.setDescription("ItemRequest1Description");
        itemRequest1.setCreated(LocalDateTime.now());

        var itemRequest2 = new ItemRequest();
        itemRequest2.setRequester(requester1);
        itemRequest2.setDescription("ItemRequest2Description");
        itemRequest2.setCreated(LocalDateTime.now());

        var itemRequest3 = new ItemRequest();
        itemRequest3.setRequester(requester2);
        itemRequest3.setDescription("ItemRequest3Description");
        itemRequest3.setCreated(LocalDateTime.now());

        tem.persist(requester1);
        tem.persist(requester2);
        tem.persist(item);
        tem.persist(itemRequest1);
        tem.persist(itemRequest2);
        tem.persist(itemRequest3);

        var itemRequests = itemRequestRepository
                .findAllByRequesterIdNot(1L, Pageable.unpaged());
        assertThat(itemRequests.size(), is(1));
    }
}
