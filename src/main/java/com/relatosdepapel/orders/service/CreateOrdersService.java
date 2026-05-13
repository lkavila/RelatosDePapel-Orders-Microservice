package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.controller.model.CreateOrderRequestDto;
import com.relatosdepapel.orders.controller.model.CreateOrderResponseDto;
import com.relatosdepapel.orders.controller.model.RequestedSupply;
import com.relatosdepapel.orders.facade.CatalogFacade;
import com.relatosdepapel.orders.facade.model.SupplyDto;
import com.relatosdepapel.orders.exception.SupplyNotFoundException;
import com.relatosdepapel.orders.repository.OrderJpaRepository;
import com.relatosdepapel.orders.repository.model.Order;
import com.relatosdepapel.orders.repository.model.OrderItem;
import com.relatosdepapel.orders.repository.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CreateOrdersService {

    private final CatalogFacade catalogFacade;
    private final OrderJpaRepository orderJpaRepository;

    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto request) {

        // Validar que la solicitud no esté vacía
        if (request.getSupplies() == null || request.getSupplies().isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un producto");
        }

        Map<SupplyDto, OrderItem> supplyOrderItemMap = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (RequestedSupply supply : request.getSupplies()) {
            SupplyDto supplyData = getSupplyData(supply);
            OrderItem orderItem = OrderItem.builder()
                    .idCatalog(supply.getId())
                    .status(OrderStatus.EN_PROCESO)
                    .quantity(supply.getQuantity())
                    .subTotal(getSubTotal(supply, supplyData))
                    .build();
            totalAmount = totalAmount.add(orderItem.getSubTotal());
            supplyOrderItemMap.put(supplyData, orderItem);
        }

        String orderName = generateOrderName();
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .total(totalAmount)
                .ownerId(1) // Se debe obtener del contexto de seguridad
                .orderItems(supplyOrderItemMap.values().stream().toList())
                .build();

        supplyOrderItemMap.values().forEach(item -> item.setOrder(order));
        Order savedOrder = orderJpaRepository.save(order);

        for (Map.Entry<SupplyDto, OrderItem> entry : supplyOrderItemMap.entrySet()) {
            updateSupplyStock(entry.getKey().getStock(), entry.getValue());
        }

        // Crear la respuesta
        return CreateOrderResponseDto.builder()
                .id(savedOrder.getId())
                .build();
    }

    private SupplyDto getSupplyData(RequestedSupply requestedSupply) {
        if (requestedSupply.getQuantity() == null || requestedSupply.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0 para el producto ID: " + requestedSupply.getId());
        }
        return catalogFacade.getSupply(requestedSupply.getId());
    }

    private BigDecimal getSubTotal(RequestedSupply requestedSupply, SupplyDto supply) {
        if (supply == null) {
            throw new SupplyNotFoundException("Producto no encontrado con ID: " + requestedSupply.getId());
        }
        if (supply.getStock() == null || supply.getStock() < requestedSupply.getQuantity()) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + supply.getName() +
                    ". Stock disponible: " + supply.getStock() + ", solicitado: " + requestedSupply.getQuantity());
        }
        BigDecimal unitPrice = supply.getPrice() != null ? supply.getPrice() : BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(requestedSupply.getQuantity()));
    }

    private String generateOrderName() {
        return "ORDER-" + System.currentTimeMillis();
    }

    private void updateSupplyStock(Integer currentStock, OrderItem item) {
        int newStock = currentStock - item.getQuantity();
        if (newStock < 0) {
            throw new IllegalArgumentException("Error crítico: el stock resultante sería negativo para el producto ID: " + item.getId());
        }
        catalogFacade.updateSupplyStock(item.getIdCatalog(), newStock);
    }
}
