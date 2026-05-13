package com.relatosdepapel.orders.repository;

import com.relatosdepapel.orders.repository.model.Order;
import com.relatosdepapel.orders.repository.model.OrderItem;
import com.relatosdepapel.orders.repository.model.OrderStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Integer>, JpaSpecificationExecutor<OrderItem> {

    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByStatus(OrderStatus status, Limit limit);

}
