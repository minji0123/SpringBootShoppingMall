package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 카트 아이디와 아이템 아이디를 이용하여 카트 아이템 레퍼지토리의 엔티티 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

}
