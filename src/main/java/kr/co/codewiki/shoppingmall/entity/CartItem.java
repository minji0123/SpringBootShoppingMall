package kr.co.codewiki.shoppingmall.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="cart_item")
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne// 카트에 아이템 많이 담을 수 있어
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne// 카트에 아이템 많이 담을 수 있어
    @JoinColumn(name = "item_id")
    private Item item;

    private int count; // 장바구니 몇개 담을 지!
}
