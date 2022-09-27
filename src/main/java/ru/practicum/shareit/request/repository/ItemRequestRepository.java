package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends CrudRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterId(Long requesterId, Sort sort);

    List<ItemRequest> findAllByRequesterIdNot(Long requesterId, Pageable pageable);
}
