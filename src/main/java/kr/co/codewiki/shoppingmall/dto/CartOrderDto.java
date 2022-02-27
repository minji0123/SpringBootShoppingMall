package kr.co.codewiki.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 주문할 상품 데이터 전달용
@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    private List<CartOrderDto> cartOrderDtoList;

}