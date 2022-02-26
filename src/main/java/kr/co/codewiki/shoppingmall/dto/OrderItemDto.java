package kr.co.codewiki.shoppingmall.dto;

import kr.co.codewiki.shoppingmall.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

// 주문 내역을 조회할 수 있는 페이지 구현 dto
// 현재 주문한 상품 정보를 볼 수 있음
@Getter
@Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){ // 주문상품, 이미지경로를 파라미터로 받음
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm; //상품명
    private int count; //주문 수량

    private int orderPrice; //주문 금액
    private String imgUrl; //상품 이미지 경로

}
