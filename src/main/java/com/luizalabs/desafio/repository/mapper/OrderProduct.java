package com.luizalabs.desafio.repository.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {

    private int orderId;
    private int userId;
    private int productId;
    private BigDecimal productValue;
    private String date;

    public static Mono<OrderProduct> mapperTo(Map<String, Object> row) {
        return Mono.just(OrderProduct.builder()
                .orderId(Integer.parseInt(row.get("orderId").toString()))
                .userId(Integer.parseInt(row.get("userId").toString()))
                .productId(Integer.parseInt(row.get("productId").toString()))
                .productValue(new BigDecimal(row.get("productValue").toString()))
                .date(row.get("date").toString())
                .build());
    }
}
