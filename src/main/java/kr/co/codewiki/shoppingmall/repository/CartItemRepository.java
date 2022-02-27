package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.dto.CartDetailDto;
import kr.co.codewiki.shoppingmall.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 카트 아이디와 아이템 아이디를 이용하여 카트 아이템 레퍼지토리의 엔티티 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // 장바구니 페이지에 전달할 CartDetailDto 를 쿼리로 조회해서 CartDetailDtoList 에 담아줌
    @Query("select new kr.co.codewiki.shoppingmall.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " + // 장바구니에 담겨있는 상품의 대표 이미지만 가져옴
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

}
