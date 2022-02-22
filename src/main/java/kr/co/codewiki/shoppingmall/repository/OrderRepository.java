package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
