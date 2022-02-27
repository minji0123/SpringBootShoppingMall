package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // 현재 로그인 된 memberId를 이용하여 유저 카트 찾기
    Cart findByMemberId(Long memberId);
}
