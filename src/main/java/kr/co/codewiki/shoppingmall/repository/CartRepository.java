package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
