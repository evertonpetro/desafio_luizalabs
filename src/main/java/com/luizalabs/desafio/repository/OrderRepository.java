package com.luizalabs.desafio.repository;

import com.luizalabs.desafio.controller.dto.Response;
import com.luizalabs.desafio.repository.model.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public interface OrderRepository {
    Mono<List<Response>> findAll();
    Flux<Response> findOrdersByOrderId(int orderId);
    Flux<Response> findOrdersByDate(String startDate, String endDate);
    Mono<Order> save(Order order);
}
