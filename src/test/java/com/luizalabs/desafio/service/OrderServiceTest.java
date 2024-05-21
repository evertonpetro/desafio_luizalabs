package com.luizalabs.desafio.service;

import com.luizalabs.desafio.exceptions.OrderNotFoundException;
import com.luizalabs.desafio.repository.OrderRepository;
import com.luizalabs.desafio.repository.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static com.luizalabs.desafio.mocks.OrderMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;
    @InjectMocks
    private OrderService service;

    @Test
    void process() {
        var filePart = mock(FilePart.class);

        when(filePart.content())
                .thenReturn(Flux.just(getDataBuffer()));

        when(repository.save(any(Order.class)))
                .thenReturn(Mono.just(Order.builder().build()));


        service.process(Flux.just(filePart))
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    @DisplayName("getAll() should return all orders")
    void getAll_shouldReturnAllOrders() {
        when(repository.findAll())
                .thenReturn(Mono.just(Collections.singletonList(getOrderResponseMock())));

        service.getAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("getByDateRange(startDate, endDate) should return orders from given dates")
    void getByDateRange_shouldReturnOrders() {
        when(repository.findOrdersByDate(anyString(), anyString()))
                .thenReturn(Flux.just(getOrderResponseMock()));

        service.getByDateRange("2024-01-01", "2024-12-31")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("getByDateRange(startDate, endDate) should return OrderNotFoundException")
    void getByDateRange_shouldReturnOrderNotFoundException() {
        when(repository.findOrdersByDate(anyString(), anyString()))
                .thenReturn(Flux.empty());

        service.getByDateRange("2024-01-01", "2024-12-31")
                .as(StepVerifier::create)
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("getByOrderId(1) should return order")
    void getByOrderId_shouldReturnOrder() {

        when(repository.findOrdersByOrderId(anyInt()))
                .thenReturn(Flux.just(getOrderResponseMock()));

        service.getByOrderId(1)
                .as(StepVerifier::create)
                .consumeNextWith(department -> assertEquals(getOrderResponseMock(), department))
                .verifyComplete();
    }

    @Test
    @DisplayName("getByOrderId(1) should return OrderNotFoundException")
    void getByOrderId_shouldReturnOrderNotFoundException() {

        when(repository.findOrdersByOrderId(anyInt()))
                .thenReturn(Flux.empty());

        service.getByOrderId(1)
                .as(StepVerifier::create)
                .expectError(OrderNotFoundException.class)
                .verify();
    }
}