package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.exception.OrderNotFoundException;
import com.relatosdepapel.orders.repository.OrderJpaRepository;
import com.relatosdepapel.orders.repository.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteOrdersService {

    private final OrderJpaRepository orderRepository;

    @Transactional
    public void deleteOrderById(Long Id) {

        Optional<Order> orders = orderRepository
                .findById(Id.intValue());

        if (orders.isEmpty()) {
            throw new OrderNotFoundException(
                    "Order not found with Id: " + Id
            );
        }
        // Obtener la orden más reciente
        Order order = orders.get();

        // Eliminar la orden
        orderRepository.delete(order);
    }
}