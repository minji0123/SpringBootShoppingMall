package kr.co.codewiki.shoppingmall.dto;

import kr.co.codewiki.shoppingmall.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

// 상품 데이터 조회 시 상품 조건을 갖고 있음
@Getter
@Setter
public class ItemSearchDto {

    // 현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회함
    private String searchDateType;
    /*
    all: 상품 등록일 전체
    1d/1w/1m/6m: 최근 하루/1주/한달/6개월 동안 등록된 상품
     */

    // 상품의 판매상태를 기준으로 상품 데이터를 조회
    private ItemSellStatus searchSellStatus;

    // 상품을 조회할 때 어떤 유형으로 조회할 지 선택
    private String searchBy;
    /*
    itemNm: 상품명
    createBy: 상품 등록자 아이디
     */

    // 조회할 검색어를 저장할 변수
    private String searchQuery = "";
    /*
    itemNm: 상품명 기준 검색
    createBy: 상품 등록자 아이디 검색
     */

}

/*
* 조회 조건이 엄청 복잡함!! (상품 등록일, 상품 판매 상태, 상품명, 상품 등록자 id...)
*
* 이렇게 조회 조건이 복잡한 화면은 Querydsl 을 이용한다.
* 1. 조건에 맞는 쿼리를 동적으로 쉽게 생성 가능
* 2. 비슷한 쿼리 재활용 가능
* 3. IDE 가 문법 오류 바로 잡아줌 (쿼리쓰면은 db 문법이기 때문에 안잡아줌)
* */