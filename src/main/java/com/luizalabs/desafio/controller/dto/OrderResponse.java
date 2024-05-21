package com.luizalabs.desafio.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    @JsonProperty("order_id")
    private int orderId;
    @JsonProperty("total")
    private BigDecimal total;
    @JsonProperty("date")
    private String date;
    @JsonProperty("products")
    private List<ProductResponse> products;
}
