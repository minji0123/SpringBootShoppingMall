package kr.co.codewiki.shoppingmall.exception;

// 상품의 주문 수량이 재고 수보다 많을 때 오류
public class OutOfStockException extends RuntimeException{

    public OutOfStockException(String message) {
        super(message);
    }

}