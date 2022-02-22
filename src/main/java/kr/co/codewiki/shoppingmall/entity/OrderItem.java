package kr.co.codewiki.shoppingmall.entity;

import kr.co.codewiki.shoppingmall.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
//@Table(name="orders")
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 한 아이템은 여러 주문에 들어갈 수 있다.
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne (fetch = FetchType.LAZY) // 한 주문에 여러 아이템이 들어갈 수 있다.
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격

    private int count; // 수량량

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
/*
 즉시 로딩: 연관된 모든 엔티티 (ex,order 엔티티와 다대일 매핑된 member 엔티티 등등...)을 함께 가지고 옴
    현재 사용하지 않아도 될 로직을 다 불러오기 때문에 보기 힘들다.
 지연 로딩: 그래서 지연 로딩 사용
 one to many 는 기본 지연 로딩

 */