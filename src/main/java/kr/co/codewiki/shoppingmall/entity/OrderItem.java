package kr.co.codewiki.shoppingmall.entity;

import kr.co.codewiki.shoppingmall.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity{ // BaseEntity: 등록한사람, 수정한사람만 있는 entity + 상속받은 등록일 수정일 entity 도 있음

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

    private int count; // 수량

//    BaseEntity 를 상속받았기 때무네 필요없어짐
//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;
    /*
 즉시 로딩: 연관된 모든 엔티티 (ex,order 엔티티와 다대일 매핑된 member 엔티티 등등...)을 함께 가지고 옴
    현재 사용하지 않아도 될 로직을 다 불러오기 때문에 보기 힘들다.
 지연 로딩: 그래서 지연 로딩 사용
 one to many 는 기본 지연 로딩

 */

    // 주문할 상품,주문 수량으로 orderItem 객체 생성
    public static OrderItem createOrderItem(Item item, int count){

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 주문할 상품 setter
        orderItem.setCount(count); // 주문 수량 setter
        orderItem.setOrderPrice(item.getPrice()); // 현재 시간 기준으로 상품가격=주문가격 (상품가격은 관리자가 세팅하는 값에 따라 달라지니까 현재 주문한 시간으로 딱 정해야 함1!!)
        item.removeStock(count); // 상품 재고 수량에서 주문 수량을 뺌

        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count; // 총 가격: 주문가격*주문수량
    }

    // Item 클래스에서 주문 취소 시 주문 수량을 상품의 재고에 더해주는 로직
    // == 주문 취소 시 주문 수량만큼 상품의 재고를 증가 (item 의 메소드 호출)
    public void cancel() {
        this.getItem().addStock(count);
    }
}
