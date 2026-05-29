package com.relatosdepapel.orders.repository;
import com.relatosdepapel.orders.repository.model.Order;
import com.relatosdepapel.orders.repository.predicate.SearchCriteria;
import com.relatosdepapel.orders.repository.predicate.SearchFields;
import com.relatosdepapel.orders.repository.predicate.SearchOperation;
import com.relatosdepapel.orders.repository.predicate.SearchStatement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    public List<Order> findByOwnerIdWithPagination(Integer ownerId, Integer page, Integer size) {
        SearchCriteria<Order> searchCriteria = new SearchCriteria<>();
        searchCriteria.add(new SearchStatement(SearchFields.OWNER_ID,ownerId, SearchOperation.EQUAL));
        return orderJpaRepository.findAll(searchCriteria, Pageable.ofSize(size).withPage(page)).getContent();
    }

    public List<Order> getOrders(
            Integer ownerId,
            LocalDateTime orderDate,
            BigDecimal minTotal,
            Integer pageSize,
            Integer page
    ) {

        SearchCriteria<Order> spec = new SearchCriteria<>();

        if (ownerId != null) {
            spec.add(
                    new SearchStatement(
                            SearchFields.OWNER_ID,
                            ownerId,
                            SearchOperation.EQUAL
                    )
            );
        }

        if (orderDate != null) {
            spec.add(
                    new SearchStatement(
                            SearchFields.ORDER_DATE,
                            orderDate,
                            SearchOperation.EQUAL
                    )
            );
        }

        if (minTotal != null) {
            spec.add(
                    new SearchStatement(
                            SearchFields.TOTAL,
                            minTotal,
                            SearchOperation.GREATER_THAN_EQUAL
                    )
            );
        }

        return orderJpaRepository.findAll(spec, Pageable.ofSize(pageSize).withPage(page)).getContent();
    }
}