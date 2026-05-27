package com.relatosdepapel.orders.repository.predicate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchFields {

    OWNER_ID("ownerId"),
    TOTAL("total"),
    ORDER_DATE("orderDate");

    private final String fieldName;
}