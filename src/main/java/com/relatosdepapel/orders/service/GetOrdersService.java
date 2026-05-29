package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.controller.model.GetOrdersResponseDto;
import com.relatosdepapel.orders.controller.model.GetOrdersOwnerResponseDto;
import com.relatosdepapel.orders.controller.model.PurchasedItem;
import com.relatosdepapel.orders.controller.model.RecentOrder;
import com.relatosdepapel.orders.exception.BadSupplyModificationException;
import com.relatosdepapel.orders.exception.InternalErrorException;
import com.relatosdepapel.orders.facade.CatalogFacade;
import com.relatosdepapel.orders.facade.model.SupplyDto;
import com.relatosdepapel.orders.controller.model.OrderDetailsDto;
import com.relatosdepapel.orders.repository.OrderJpaRepository;
import com.relatosdepapel.orders.repository.OrderRepository;
import com.relatosdepapel.orders.repository.model.Order;
import com.relatosdepapel.orders.repository.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.relatosdepapel.orders.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



@Service
@RequiredArgsConstructor
public class GetOrdersService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderRepository orderRepository;
    private final CatalogFacade catalogFacade;

    @Transactional(readOnly = true)
    public GetOrdersResponseDto getRecentOrders() {
        List<Order> recentOrders = orderJpaRepository.findAll().stream().toList();
        Map<Integer, SupplyDto> suppliesMap = getSuppliesInBatch(recentOrders);
        List<RecentOrder> mappedOrders = buildRecentOrders(recentOrders, suppliesMap);
        return GetOrdersResponseDto.builder()
                .recentOrders(mappedOrders)
                .build();
    }

    public GetOrdersResponseDto getOrders(
            Integer ownerId,
            LocalDateTime orderDate,
            BigDecimal minTotal,
            Integer pageSize,
            Integer page
    ) {

        List<Order> orders = orderRepository.getOrders(
                ownerId,
                orderDate,
                minTotal,
                pageSize,
                page
        );

        List<RecentOrder> recentOrders = buildRecentOrders(orders, getSuppliesInBatch(orders));

        return GetOrdersResponseDto.builder()
                .recentOrders(recentOrders)
                .build();
    }

    @Transactional(readOnly = true)
    public GetOrdersOwnerResponseDto getOrderByOwnerId(Integer ownerId,Integer page, Integer pageSize) {
        List<Order> recentOrders = orderRepository.findByOwnerIdWithPagination(ownerId,page,pageSize);

        if (recentOrders.isEmpty()) {
            throw new BadSupplyModificationException("No orders found for ownerId: " + ownerId);
        }
        List<OrderDetailsDto> orderDetailsList = recentOrders.stream()
                .map(o -> OrderDetailsDto.builder()
                        .id(Long.valueOf(o.getId()))
                        .order_date(o.getOrderDate().toString())
                        .total(o.getTotal().longValue())
                        .comment(o.getComment())
                        .updated_at(o.getUpdatedAt().toString())
                        .build())
                .collect(Collectors.toList());
        return GetOrdersOwnerResponseDto.builder()
                .ownerId(Long.valueOf(ownerId))
                .pageSize(pageSize)
                .currentPage(page)
                .ordersDetails(orderDetailsList)
                .build();
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
                .id(supply.getId())
                .name(supply.getTitle())
                .status(orderItem.getStatus().name())
                .price(supply.getPrice().doubleValue())
                .quantity(orderItem.getQuantity())
                .build();
    }

    private Map<Integer, SupplyDto> getSuppliesInBatch(List<Order> orders) {
        List<Integer> suppliesIds = orders.stream()
                .map(Order::getOrderItems).map(
                        items -> items.stream().map(OrderItem::getIdCatalog).toList()
                ).flatMap(List::stream).toList();

        int maxBatch = 2500;
        if (suppliesIds.size() <= maxBatch) {
            return catalogFacade.getSuppliesInBatch(suppliesIds);
        }
        Map<Integer, SupplyDto> allResults = new HashMap<>();
        int totalIds = suppliesIds.size();

        for (int i = 0; i < totalIds; i += maxBatch) {
            int end = Math.min(i + maxBatch, totalIds);
            List<Integer> currentList = suppliesIds.subList(i, end);
            Map<Integer, SupplyDto> batch = catalogFacade.getSuppliesInBatch(currentList);
            allResults.putAll(batch);
        }
        return allResults;
    }

    private List<RecentOrder> buildRecentOrders(List<Order> recentOrders, Map<Integer, SupplyDto> supplies) {
        return recentOrders.stream()
                .map(order -> {
                    List<PurchasedItem> purchasedItems = order.getOrderItems().stream()
                            .map(orderItem -> {
                                SupplyDto supply = supplies.get(orderItem.getIdCatalog());
                                return PurchasedItem.builder()
                                        .id(supply.getId())
                                        .name(supply.getTitle())
                                        .status(orderItem.getStatus().name())
                                        .price(supply.getPrice().doubleValue())
                                        .quantity(orderItem.getQuantity())
                                        .build();
                            })
                            .toList();
                    return RecentOrder.builder()
                            .id(order.getId())
                            .total(order.getTotal().doubleValue())
                            .date(order.getOrderDate().toLocalDate().toString())
                            .comment(order.getComment())
                            .items(purchasedItems)
                            .build();
                })
                .toList();

    }
}
