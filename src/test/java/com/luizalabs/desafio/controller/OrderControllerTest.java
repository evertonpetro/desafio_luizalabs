package com.luizalabs.desafio.controller;

import com.luizalabs.desafio.controller.dto.Response;
import com.luizalabs.desafio.exceptions.OrderNotFoundException;
import com.luizalabs.desafio.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.luizalabs.desafio.mocks.OrderMock.getOrderResponseMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService service;

    @Test
    @DisplayName("POST /v1/orders/upload should return success")
    void upload_shouldReturnSuccessForUpload() {

        when(this.service.process(any()))
                .thenReturn(Mono.empty());
        
        webTestClient.post()
                .uri("/v1/orders/upload")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> assertEquals("Dados inseridos com sucesso", response.getResponseBody()));
    }

    @Test
    @DisplayName("GET /v1/orders should return all orders")
    void getAll_shouldReturnAllOrders() {

        when(this.service.getAll())
                .thenReturn(Flux.just(getOrderResponseMock()));

        webTestClient.get()
                .uri("/v1/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Response.class).hasSize(1)
                .consumeWith(response -> assertEquals(List.of(getOrderResponseMock()), response.getResponseBody()));
    }

    @Test
    @DisplayName("GET /v1/orders/1 should return order from given order_id")
    void getByOrderId_shouldReturnOrderFromGivenOrderId() {

        when(this.service.getByOrderId(anyInt()))
                .thenReturn(Mono.just(getOrderResponseMock()));

        webTestClient.get()
                .uri("/v1/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Response.class).hasSize(1)
                .consumeWith(response -> assertEquals(List.of(getOrderResponseMock()), response.getResponseBody()));
    }

    @Test
    @DisplayName("GET /v1/orders/1 should return OrderNotFoundException")
    void getByOrderId_shouldReturnNotFoundException() {

        when(this.service.getByOrderId(anyInt()))
                .thenThrow(new OrderNotFoundException(1));

        webTestClient.get()
                .uri("/v1/orders/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith(exception -> assertEquals("order_id 1 not found.", exception.getResponseBody()));
    }

    @Test
    @DisplayName("GET /v1/orders?startDate=2024-01-01&endDate=2024-12-31 should return orders from given dates")
    void getByDateRange_shouldReturnOrdersFromGivenDates() {
        when(this.service.getByDateRange(anyString(), anyString()))
                .thenReturn(Flux.just(getOrderResponseMock()));

        webTestClient.get()
                .uri("/v1/orders?startDate=2024-01-01&endDate=2024-12-31")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Response.class).hasSize(1)
                .consumeWith(response -> assertEquals(List.of(getOrderResponseMock()), response.getResponseBody()));
    }

    @Test
    @DisplayName("GET /v1/orders?startDate=2024-01-01&endDate=2024-12-31 return OrderNotFoundException")
    void getByDateRange_shouldReturnNotFoundException() {
        when(this.service.getByDateRange(anyString(), anyString()))
                .thenThrow(new OrderNotFoundException("2024-01", "2024-12-31"));

        webTestClient.get()
                .uri("/v1/orders?startDate=2024-01-01&endDate=2024-12-31")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .consumeWith(exception -> assertEquals("orders not found from 2024-01 and 2024-12-31 dates.", exception.getResponseBody()));
    }
}