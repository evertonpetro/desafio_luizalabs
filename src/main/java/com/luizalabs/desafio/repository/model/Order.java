package com.luizalabs.desafio.repository.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class Order {
    private int orderId;
    private User user;
    private List<Product> product;
    private String date;
}