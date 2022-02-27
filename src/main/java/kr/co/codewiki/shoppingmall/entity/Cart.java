package kr.co.codewiki.shoppingmall.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "cart")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity{

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @OneToOne(fetch = FetchType.EAGER) // 즉시 로딩: 회원 엔티티를 조회할 때 장바구니 엔티티도 같이 조회함
    @OneToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "member_id") // 외래키 지정!
    private Member member;

    // 카트에 유저 할당하여 넣어줌
    // 회원 한명당 장바구니를 하나씩 갖기 때문에 할당해주는거임
    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

}
