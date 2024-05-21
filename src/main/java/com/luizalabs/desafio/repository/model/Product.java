package com.luizalabs.desafio.repository.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int productId;
    private BigDecimal productValue;
}
