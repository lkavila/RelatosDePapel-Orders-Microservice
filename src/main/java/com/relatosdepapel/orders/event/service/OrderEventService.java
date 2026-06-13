package com.relatosdepapel.orders.event.service;

import com.relatosdepapel.orders.event.model.OrderCreatedEvent;
import com.relatosdepapel.orders.event.model.OrderCreatedEventBody;
import com.relatosdepapel.orders.event.model.OrderItemEvent;
import com.relatosdepapel.orders.event.model.EventHeader;
import com.relatosdepapel.orders.repository.model.OrderItem;
import com.relatosdepapel.orders.repository.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j

public class OrderEventService {

    private final RabbitTemplate rabbitTemplate;

    @org.springframework.beans.factory.annotation.Value("${rabbitmq.exchange.orders}")
    private String ordersExchange;

    @Value("${rabbitmq.routing.key.order.created}")
    private String orderCreatedRoutingKey;

    public void publishOrderCreatedEvent(Order order) {
        try {
            OrderCreatedEvent event = buildOrderCreatedEvent(order);
            rabbitTemplate.convertAndSend(ordersExchange, orderCreatedRoutingKey, event);
            log.info("Evento de pedido creado publicado exitosamente. Order: {}, EventId: {}",
                    order.getId(), event.getHeader().getEventId());
        } catch (Exception e) {
            log.error("Error al publicar evento de pedido creado para order: {}", order.getId(), e);
            // No relanzamos la excepción para no afectar la creación del pedido
        }
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Order order) {
        String eventId = UUID.randomUUID().toString();

        EventHeader header = EventHeader.builder()
                .eventId(eventId)
                .version("1.0")
                .eventType("ORDER_CREATED")
                .timestamp(LocalDateTime.now())
                .build();

        List<OrderItemEvent> orderItemEvents = order.getOrderItems().stream()
                .map(this::mapToOrderItemEvent)
                .toList();

        OrderCreatedEventBody body = OrderCreatedEventBody.builder()
                .orderName(String.valueOf(order.getId()))
                .orderDate(order.getOrderDate())
                .total(order.getTotal())
                .comment(order.getComment())
                .ownerId(order.getOwnerId())
                .orderItems(orderItemEvents)
                .build();

        return OrderCreatedEvent.builder()
                .header(header)
                .body(body)
                .build();
    }

    private OrderItemEvent mapToOrderItemEvent(OrderItem orderItem) {
        return OrderItemEvent.builder()
                .idCatalog(orderItem.getIdCatalog())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem.getSubTotal())
                .build();
    }

}
