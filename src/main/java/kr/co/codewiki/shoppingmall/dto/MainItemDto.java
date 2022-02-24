package kr.co.codewiki.shoppingmall.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

// 메인 페이지에서 상품을 보여줄 때 사용할 dto
@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;


    @QueryProjection // entity 로 원래 바꿨었는데, 이 어노테이션을 쓰면은 dto 로 객체를 변환할 수 있다.
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl,Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}