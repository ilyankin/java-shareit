package ru.practicum.shareit.item.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where i.owner.id = ?1 " +
            "order by i.id asc")
    List<Item> findAllByOwnerId(Long userId);

    @Query("select i from Item i " +
            "where lower(i.name) like lower(concat('%', ?1, '%')) " +
            "or lower(i.description) like lower(concat('%', ?1, '%')) " +
            "and i.available = true " +
            "order by i.id")
    List<Item> searchByText(String text);
}
