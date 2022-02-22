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

    @ManyToOne // 한 아이템은 여러 주문에 들어갈 수 있다.
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne // 한 주문에 여러 아이템이 들어갈 수 있다.
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격

    private int count; // 수량량

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
