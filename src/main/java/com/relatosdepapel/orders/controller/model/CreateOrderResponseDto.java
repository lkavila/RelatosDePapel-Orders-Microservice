package com.relatosdepapel.orders.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id"
})
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOrderResponseDto implements Serializable {

    private final static long serialVersionUID = 7686450847709803303L;

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("ownerId")
    public Integer ownerId;
}
