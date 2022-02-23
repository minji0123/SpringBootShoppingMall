package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 상품의 이미지 정보 쿼리 repository
public interface ItemImgRepository extends JpaRepository <ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId); // itemServiceTest 를 위해서 만들었움

}
