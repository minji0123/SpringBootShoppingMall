package kr.co.codewiki.shoppingmall.entity;

import kr.co.codewiki.shoppingmall.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order extends BaseEntity{ // 등록한사람, 수정한사람만 있는 entity + 상속받은 등록일 수정일 entity 도 있음

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 한 명의 회원은 여러 번 주문을 할 수 있다
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

//    BaseEntity 를 상속받았기 때무네 필요없어짐
//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;

    // cascade:  부모가 바뀌면 자식도 바뀌고! 부모랑 자식이랑 연동됨. 몰아일체라고 할 수 있지
    // orphanRemoval = true 를 하면은 고아 객체를 지울 수 있다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // 원래 디비에는 one to many 가 없다. 단방향이기 때문에! => 양방향 매핑을 위해 적음 (order - orderId)
    private List<OrderItem> orderItems = new ArrayList<>();



    // 생성한 주문 상품 객체를 이용해서 주문 객체를 만들거임
    public void addOrderItem(OrderItem orderItem) {

        orderItems.add(orderItem); // orderItems 에 주문 상품 정보들 넣어줌
        orderItem.setOrder(this); // orderItems 과 양방향 매핑이기 때문에 orderItem 에다가도 order 객체를 넣어줌 (orderItems 은 order 객체임)

    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {

        Order order = new Order();
        order.setMember(member); // 상품을 주문한 회원의 정보 setter

        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
            // 상품 페이지에서는 1개의 상품을 주문하지만, 장바구니에는 여러 상품을 주문할 수 있다.
            // 그래서 장바구니에 여러 상품을 담을 수 있게 리스트 형태로 파라미터 값을 받아야 한다.  파라미터는 아까 주문한 orderItem 임!

        }

        order.setOrderStatus(OrderStatus.ORDER); // 주문 상태를 ORDER 로 바꿈
        order.setOrderDate(LocalDateTime.now()); // 현재 시간을 주문 시간으로 바꿈
        return order;
    }

    // 총 주문 금액
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }



}
