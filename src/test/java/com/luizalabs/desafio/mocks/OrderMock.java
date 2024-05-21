package com.luizalabs.desafio.mocks;

import com.luizalabs.desafio.controller.dto.OrderResponse;
import com.luizalabs.desafio.controller.dto.ProductResponse;
import com.luizalabs.desafio.controller.dto.Response;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

public class OrderMock {

    public static Response getOrderResponseMock() {
        return Response.builder()
                .userId(1)
                .name("user name")
                .orders(Collections.singletonList(OrderResponse.builder()
                        .orderId(2)
                        .total(BigDecimal.TEN)
                        .date(LocalDate.now().toString())
                        .products(Collections.singletonList(ProductResponse.builder()
                                .productId(3)
                                .productValue(BigDecimal.TEN)
                                .build()))
                        .build()))
                .build();
    }

    public static DefaultDataBuffer getDataBuffer() {
        var bufferFactory = new DefaultDataBufferFactory();

        byte[] bytes = """
                    0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308
                    0000000075                                  Bobbie Batz00000007980000000002     1578.5720211116
                    0000000049                               Ken Wintheiser00000005230000000003      586.7420210903
                    0000000014                                 Clelia Hills00000001460000000001      673.4920211125
                    0000000057                          Elidia Gulgowski IV00000006200000000000     1417.2520210919""".getBytes();

        var buffer = bufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }
}
