package com.relatosdepapel.orders.controller.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "status",
        "quantity",
        "price"
})
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PurchasedItem implements Serializable {

    @Serial
    private final static long serialVersionUID = -4761762119375139021L;

    @JsonProperty("name")
    public String name;
    @JsonProperty("status")
    private String status;
    @JsonProperty("quantity")
    public Integer quantity;
    @JsonProperty("price")
    public Double price;

}
