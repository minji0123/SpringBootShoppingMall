package kr.co.codewiki.shoppingmall.repository;


import kr.co.codewiki.shoppingmall.dto.ItemSearchDto;
import kr.co.codewiki.shoppingmall.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 사용자 정의 인터페이스 작성
public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
