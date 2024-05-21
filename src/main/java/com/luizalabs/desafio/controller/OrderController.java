package com.luizalabs.desafio.controller;

import com.luizalabs.desafio.controller.dto.Response;
import com.luizalabs.desafio.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService service;

    @PostMapping("/upload")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<String> upload(@RequestPart("file") Flux<FilePart> filePartFlux) {
        return service.process(filePartFlux)
                .thenReturn("Dados inseridos com sucesso");
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<Response> getByOrderId(@PathVariable int orderId) {
        return service.getByOrderId(orderId);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Flux<Response> getByDateRange(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {

        return  isNullOrEmpty(startDate) && isNullOrEmpty(endDate)
                ? service.getAll()
                : service.getByDateRange(startDate, endDate);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}

