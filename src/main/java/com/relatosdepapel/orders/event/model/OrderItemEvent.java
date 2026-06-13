package com.relatosdepapel.orders.event.model;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder

public class OrderItemEvent {
    private Integer idCatalog;
    private Integer quantity;
    private BigDecimal subTotal;
}
