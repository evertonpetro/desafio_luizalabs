package com.luizalabs.desafio.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private int user_id;
    private String name;
    private int order_id;
    private int product_id;
    private BigDecimal value;
    private String date;
}
