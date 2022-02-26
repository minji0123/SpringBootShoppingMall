package kr.co.codewiki.shoppingmall.dto;

import kr.co.codewiki.shoppingmall.constant.OrderStatus;
import kr.co.codewiki.shoppingmall.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 주문 정보를 담을 dto
@Getter
@Setter
public class OrderHistDto {

    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId; //주문아이디
    private String orderDate; //주문날짜
    private OrderStatus orderStatus; //주문 상태

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    //주문 상품리스트
    public void addOrderItemDto(OrderItemDto orderItemDto){ // '현재 주문한 상품 정보' 를 주문 상품 리스트에 담음음
       orderItemDtoList.add(orderItemDto);
    }
}
