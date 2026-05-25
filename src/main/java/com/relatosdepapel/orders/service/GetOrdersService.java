package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.controller.model.GetOrdersResponseDto;
import com.relatosdepapel.orders.controller.model.PurchasedItem;
import com.relatosdepapel.orders.controller.model.RecentOrder;
import com.relatosdepapel.orders.exception.OrdersNotFoundException;
import com.relatosdepapel.orders.facade.CatalogFacade;
import com.relatosdepapel.orders.facade.model.SupplyDto;
import com.relatosdepapel.orders.repository.OrderJpaRepository;
import com.relatosdepapel.orders.repository.model.Order;
import com.relatosdepapel.orders.repository.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetOrdersService {

    private final OrderJpaRepository orderJpaRepository;
    private final CatalogFacade catalogFacade;

    @Transactional(readOnly = true)
    public GetOrdersResponseDto getRecentOrders() {
        List<Order> recentOrders = orderJpaRepository.findAll().stream().toList();
        return GetOrdersResponseDto.builder()
                .recentOrders(recentOrders.stream().map(this::getRecentOrder).toList())
                .build();
    }

    @Transactional(readOnly = true)
    public GetOrdersResponseDto getOrderByOwnerId(Integer ownerId) {
        Optional<Order> recentOrders = orderJpaRepository.findById(ownerId);
        return recentOrders.map(
                o ->  GetOrdersResponseDto.builder()
                        .id(Long.valueOf(o.getId()))
                        .order_date(o.getOrderDate().toString())
                        .total(o.getTotal().longValue())
                        .comment(o.getComment())
                        .ownerId(o.getOwnerId().longValue())
                        .updated_at(o.getUpdatedAt().toString())
                        .build()
        ).orElseThrow(
                () -> new OrdersNotFoundException("ownerId not found with id:"+ownerId));

    }
    private RecentOrder getRecentOrder(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        List<PurchasedItem> purchasedItems = orderItems.stream().map(this::getSupplyData).toList();
        return RecentOrder.builder()
                .id(order.getId())
                .total(order.getTotal().doubleValue())
                .date(order.getOrderDate().toLocalDate().toString())
                .comment(order.getComment())
                .items(purchasedItems)
                .build();
    }

    private PurchasedItem getSupplyData(OrderItem orderItem) {
        SupplyDto supply = catalogFacade.getSupply(orderItem.getIdCatalog());
        return PurchasedItem.builder()
                .name(supply.getName())
                .status(orderItem.getStatus().name())
                .price(supply.getPrice().doubleValue())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
