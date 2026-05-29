package com.relatosdepapel.orders.repository;

import com.relatosdepapel.orders.repository.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Integer>,
                                            JpaSpecificationExecutor<Order>,
                                            PagingAndSortingRepository<Order, Integer> {



    List<Order> findByOwnerIdOrderByOrderDateDesc(Integer ownerId);

    List<Order> findByOwnerId(Integer ownerId);

    Optional<Order> findById(Integer Id);

    class OrderRepository {
    }
}