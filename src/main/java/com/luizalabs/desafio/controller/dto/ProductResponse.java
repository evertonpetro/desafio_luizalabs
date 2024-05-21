package com.luizalabs.desafio.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    @JsonProperty("product_id")
    private int productId;
    @JsonProperty("value")
    private BigDecimal productValue;
}
