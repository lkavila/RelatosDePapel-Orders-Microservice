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
        "id",
        "date",
        "total",
        "comment",
        "items"
})
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecentOrder implements Serializable {

    @Serial
    private final static long serialVersionUID = 2923892800706299020L;

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("date")
    private String date;
    @JsonProperty("total")
    private Double total;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("items")
    private List<PurchasedItem> items;

}
