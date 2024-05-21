package com.luizalabs.desafio.repository.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUser {

    private int userId;
    private String name;

    public static Mono<OrderUser> mapperTo(Map<String, Object> row) {
        return Mono.just(OrderUser.builder()
                .userId(Integer.parseInt(row.get("ID").toString()))
                .name(row.get("NAME").toString())
                .build());
    }
}
