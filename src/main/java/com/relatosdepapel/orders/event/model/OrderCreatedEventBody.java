package com.relatosdepapel.orders.event.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderCreatedEventBody {
    private String orderName;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
    private Integer ownerId;
    private String comment;
    private List<OrderItemEvent> orderItems;
}

