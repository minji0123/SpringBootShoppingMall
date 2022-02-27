package kr.co.codewiki.shoppingmall.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 카트에 아이템 많이 담을 수 있어
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)  // 장바구니 몇개 담을지!
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    // 장바구니에 담을 상품 엔티티 생성 메소드
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    // 장바구니에 담을 상품 수량 증가
    public void addCount(int count){
        this.count += count;
    }

//    // 장바구니 담을 수량 업데이트
//    public void updateCount(int count){
//        this.count = count;
//    }
}
