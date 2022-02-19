package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> {

    // 쿼리 메소드: find + (엔티티이름) + By+ 변수이름
    // 상품명으로 데이터 조회하기
    List<Item> findByItemNm(String itemNm);

    // or 조건 처리하기
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
}
