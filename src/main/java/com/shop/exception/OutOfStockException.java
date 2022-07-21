package com.shop.exception;

public class OutOfStockException extends RuntimeException{

//    재고의 수가 적을 때 발생시킬 예외
    public OutOfStockException(String message) {
        super(message);
    }

}