package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
