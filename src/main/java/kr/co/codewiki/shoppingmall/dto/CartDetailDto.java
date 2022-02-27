package kr.co.codewiki.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 장바구니 조회 페이지에 전달
// 장바구니에 들어있는 상품들을 조회하기 위해서!
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDto {

    private Long cartItemId; //장바구니 상품 아이디

    private String itemNm; //상품명

    private int price; //상품 금액

    private int count; //수량

    private String imgUrl; //상품 이미지 경로

//    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl){ // 장바구니에 전달할 애들인데, 그냥 어노테이션 씀
//        this.cartItemId = cartItemId;
//        this.itemNm = itemNm;
//        this.price = price;
//        this.count = count;
//        this.imgUrl = imgUrl;
//    }
}
