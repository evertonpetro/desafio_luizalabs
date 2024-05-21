package com.luizalabs.desafio.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("orders")
    private List<OrderResponse> orders;
}
