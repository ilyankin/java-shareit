package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager tem;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findAllByOwnerId() {
        var user1 = new User();
        user1.setEmail("user1@mail.com");
        user1.setName("User1Name");

        var user2 = new User();
        user2.setEmail("user2@mail.com");
        user2.setName("User2Name");

        tem.persist(user1);
        tem.persist(user2);

        var item1Owner1 = new Item();
        item1Owner1.setName("item1Owner1");
        item1Owner1.setDescription("item1Owner1Description");
        item1Owner1.setAvailable(true);
        item1Owner1.setOwner(user1);

        var item3Owner2 = new Item();
        item3Owner2.setName("item3Owner2");
        item3Owner2.setDescription("item3Owner2Description");
        item3Owner2.setAvailable(true);
        item3Owner2.setOwner(user2);

        var item2Owner1 = new Item();
        item2Owner1.setName("item2Owner1");
        item2Owner1.setDescription("item2Owner1Description");
        item2Owner1.setAvailable(true);
        item2Owner1.setOwner(user1);

        tem.persist(item1Owner1);
        tem.persist(item2Owner1);
        tem.persist(item3Owner2);

        var result = itemRepository.findAllByOwnerId(user1.getId(), Pageable.unpaged());
        assertThat(result.size(), is(2));
        assertThat(result, is(List.of(item1Owner1, item2Owner1)));
    }

    @Test
    void searchByText() {
        var user = new User();
        user.setEmail("user@mail.com");
        user.setName("UserName");

        tem.persist(user);

        var item1 = new Item();
        item1.setName("item1");
        item1.setDescription("item1Description");
        item1.setAvailable(true);
        item1.setOwner(user);

        var item2 = new Item();
        item2.setName("item2");
        item2.setDescription("item2Description");
        item2.setAvailable(true);
        item2.setOwner(user);

        var item3 = new Item();
        item3.setName("stuff");
        item3.setDescription("stuffDescription");
        item3.setAvailable(true);
        item3.setOwner(user);

        tem.persist(item1);
        tem.persist(item2);
        tem.persist(item3);

        var result = itemRepository.searchByText("item", Pageable.unpaged());
        assertThat(result.size(), is(2));
        assertThat(result, is(List.of(item1, item2)));
    }
}
