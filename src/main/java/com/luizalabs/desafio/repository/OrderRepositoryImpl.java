package com.luizalabs.desafio.repository;

import com.luizalabs.desafio.controller.dto.OrderResponse;
import com.luizalabs.desafio.controller.dto.ProductResponse;
import com.luizalabs.desafio.controller.dto.Response;
import com.luizalabs.desafio.repository.mapper.OrderProduct;
import com.luizalabs.desafio.repository.mapper.OrderUser;
import com.luizalabs.desafio.repository.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final DatabaseClient databaseClient;
    private final UserRepository userRepository;

    private static final String SELECT_ORDERS_USERS_QUERY = """
            select u.id,
                   u.name,
              from users u
             inner join orders o
                on u.id = o.userId
             %s
             group by u.id,
                      u.name
              order by u.id
            """;
    private static final String SELECT_ORDERS_PRODUCTS_QUERY = """
            select o.orderId,
                   o.userId,
                   op.productId,
                   op.productValue,
                   o.date
              from orders o
              inner join order_products op
                 on o.orderId = op.orderId
              %s
              group by o.orderId,
                   o.userId,
                   op.productId,
                   op.productValue,
                   o.date
              order by o.orderId
            """;

    @Override
    public Mono<List<Response>> findAll() {
        return this.find(String.format(SELECT_ORDERS_USERS_QUERY, ""),
                String.format(SELECT_ORDERS_PRODUCTS_QUERY, ""));
    }

    @Override
    public Flux<Response> findOrdersByOrderId(int orderId) {

        var condition = String.format("WHERE o.orderId = %d", orderId);

        return this.find(String.format(SELECT_ORDERS_USERS_QUERY, condition),
                String.format(SELECT_ORDERS_PRODUCTS_QUERY, condition))
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Response> findOrdersByDate(String startDate, String endDate) {
        var condition = String.format("WHERE o.date between '%s' and '%s'", startDate, endDate);

        return this.find(String.format(SELECT_ORDERS_USERS_QUERY, condition),
                        String.format(SELECT_ORDERS_PRODUCTS_QUERY, condition))
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Order> save(Order order) {
        return this.saveUser(order)
                .flatMap(this::saveOrder)
                .flatMap(this::saveOrderProducts);
    }

    private Mono<Order> saveUser(Order order) {
        return userRepository.findById(order.getUser().getId())
                .switchIfEmpty(userRepository.save(order.getUser()))
                .thenReturn(order);
    }

    private Mono<Order> saveOrder(Order order) {
        return databaseClient.sql("INSERT INTO orders(orderId, userId, date) VALUES(:orderId, :userId, :date)")
                .bind("orderId", order.getOrderId())
                .bind("userId", order.getUser().getId())
                .bind("date", order.getDate())
                .fetch().first()
                .thenReturn(order);
    }

    private Mono<Order> saveOrderProducts(Order order) {
        return Flux.fromIterable(order.getProduct())
                .flatMap(product -> databaseClient.sql("INSERT INTO order_products(orderId, productId, productValue) VALUES(:orderId, :productId, :productValue)")
                        .bind("orderId", order.getOrderId())
                        .bind("productId", product.getProductId())
                        .bind("productValue", product.getProductValue())
                        .fetch().rowsUpdated())
                .collectList()
                .thenReturn(order);

    }

    private Mono<List<Response>> find(String ordersUsersQuery, String ordersProductsQuery) {
        return Mono.zip(this.getOrderUser(ordersUsersQuery),
                        this.getOrderProduct(ordersProductsQuery))
                .map(objects -> objects.getT1()
                        .stream().map(orderUser -> {
                            var response = Response.builder();
                            response.userId(orderUser.getUserId());
                            response.name(orderUser.getName());
                            response.orders(objects.getT2()
                                    .stream().filter(orderProduct -> orderProduct.getUserId() == orderUser.getUserId())
                                    .collect(groupingBy(OrderProduct::getOrderId, groupingBy(OrderProduct::getDate)))
                                    .entrySet()
                                    .stream()
                                    .map(mapEntry -> {
                                        var orderResponse = OrderResponse.builder();
                                        orderResponse.orderId(mapEntry.getKey());
                                        mapEntry.getValue()
                                                .forEach((date, orderProducts) -> {
                                                    orderResponse.total(orderProducts.stream().map(OrderProduct::getProductValue).reduce(BigDecimal.ZERO, BigDecimal::add));
                                                    orderResponse.date(String.valueOf(date));
                                                    orderResponse.products(orderProducts.stream().map(o -> ProductResponse.builder()
                                                            .productId(o.getProductId())
                                                            .productValue(o.getProductValue())
                                                            .build()).toList());
                                                });
                                        return orderResponse.build();
                                    }).toList());
                            return response.build();
                        })
                        .toList());
    }

    private Mono<List<OrderUser>> getOrderUser(String query) {
        return databaseClient.sql(query)
                .fetch()
                .all()
                .flatMap(OrderUser::mapperTo)
                .collectList();
    }

    private Mono<List<OrderProduct>> getOrderProduct(String query) {
        return databaseClient.sql(query)
                .fetch()
                .all()
                .flatMap(OrderProduct::mapperTo)
                .collectList();
    }
}
