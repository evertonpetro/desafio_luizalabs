package com.luizalabs.desafio.service;

import com.luizalabs.desafio.controller.dto.Request;
import com.luizalabs.desafio.controller.dto.Response;
import com.luizalabs.desafio.exceptions.OrderNotFoundException;
import com.luizalabs.desafio.repository.OrderRepository;
import com.luizalabs.desafio.repository.model.Order;
import com.luizalabs.desafio.repository.model.Product;
import com.luizalabs.desafio.repository.model.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.springframework.core.io.buffer.DataBufferUtils;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Mono<Void> process(Flux<FilePart> filePartFlux) {
        return filePartFlux.flatMap(filePart -> filePart.content()
                        .map(dataBuffer -> {
                            var bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            return new String(bytes, StandardCharsets.UTF_8);
                        })
                        .map(this::getOrders))
                .flatMap(orders -> Flux.fromIterable(orders).flatMap(this.orderRepository::save))
                .then();
    }

    public Flux<Response> getAll(){
        return orderRepository.findAll()
                .flatMapIterable(orderResponse -> orderResponse);
    }

    public Flux<Response> getByDateRange(String startDate, String endDate){
        return orderRepository.findOrdersByDate(startDate, endDate)
                .switchIfEmpty(Mono.error(new OrderNotFoundException(startDate, endDate)));
    }

    public Mono<Response> getByOrderId(int orderId) {
        return orderRepository.findOrdersByOrderId(orderId)
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(new OrderNotFoundException(orderId)));
    }

    private List<Order> getOrders(String data) {
        var list = new ArrayList<Order>();
        transform(data)
                .forEach(ol -> list.stream().filter(order -> Objects.equals(order.getOrderId(), ol.getOrder_id()))
                        .findFirst()
                        .ifPresentOrElse(order -> {
                                    var products = new ArrayList<>(Collections.singletonList(Product.builder()
                                            .productId(ol.getProduct_id())
                                            .productValue(ol.getValue())
                                            .build()));
                                    products.addAll(order.getProduct());
                                    order.setProduct(products);
                                },
                                () -> list.add(Order.builder()
                                        .orderId(ol.getOrder_id())
                                        .user(User.builder()
                                                .id(ol.getUser_id())
                                                .name(ol.getName())
                                                .build())
                                        .product(Collections.singletonList(Product.builder()
                                                .productId(ol.getProduct_id())
                                                .productValue(ol.getValue())
                                                .build()))
                                        .date(ol.getDate())
                                        .build())));
        return list;
    }

    private Stream<Request> transform(String data) {
        return data.lines()
                .map(line -> Request.builder()
                        .user_id(Integer.parseInt(line.substring(0, 10)))
                        .name(line.substring(11, 55).trim())
                        .order_id(Integer.parseInt(line.substring(56, 65).trim()))
                        .product_id(Integer.parseInt(line.substring(66, 75).trim()))
                        .value(new BigDecimal(line.substring(76, 87).trim()))
                        .date(formatDate(line.substring(87).trim()))
                        .build());
    }

    @SneakyThrows
    private String formatDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE)
                .toString();
    }
}
