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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne // 한 명의 회원은 여러 번 주문을 할 수 있다
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    // cascade 부모가 바뀌면 자식도 바뀌고! 부모랑 자식이랑 연동됨. 몰아일체라고 할 수 있지
    // orphanRemoval = true 를 하면은 고아 객체를 지울 수 있다
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // 양방향 매핑을 위해 적음 (order - orderId)
//    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

}
