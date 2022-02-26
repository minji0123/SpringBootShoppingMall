package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 상품의 이미지 정보 쿼리 repository
public interface ItemImgRepository extends JpaRepository <ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId); // itemServiceTest 를 위해서 만들었움

    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn); // 상품 대표 이미지를 찾는 쿼리 메소드 추가
    // 하는 이유는, 구매 이력 페이지에서 주문 상품 대표 이미지 출력할려고

}
