package com.luizalabs.desafio.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(int orderId) {
        super(String.format("order_id %d not found.", orderId));
    }

    public OrderNotFoundException(String startDate, String endDate) {
        super(String.format("orders not found from %s and %s dates.", startDate, endDate));
    }
}
