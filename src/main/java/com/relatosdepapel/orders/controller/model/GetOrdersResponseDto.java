package com.relatosdepapel.orders.controller.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "recentOrders"
})

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetOrdersResponseDto implements Serializable {

    @Serial
    private final static long serialVersionUID = -8949888676747079614L;
    @JsonProperty("recentOrders")
    public List<RecentOrder> recentOrders;
    @JsonProperty("id")
    public Long id;
    @JsonProperty("ownerId")
    public Long ownerId;
    public List<OrderDetailsDto> ordersDetails;






}
