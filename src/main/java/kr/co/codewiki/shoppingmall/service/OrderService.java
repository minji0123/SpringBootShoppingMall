package kr.co.codewiki.shoppingmall.service;

import kr.co.codewiki.shoppingmall.dto.OrderDto;
import kr.co.codewiki.shoppingmall.entity.Item;
import kr.co.codewiki.shoppingmall.entity.Member;
import kr.co.codewiki.shoppingmall.entity.Order;
import kr.co.codewiki.shoppingmall.entity.OrderItem;
import kr.co.codewiki.shoppingmall.repository.ItemImgRepository;
import kr.co.codewiki.shoppingmall.repository.ItemRepository;
import kr.co.codewiki.shoppingmall.repository.MemberRepository;
import kr.co.codewiki.shoppingmall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

// 상품 주문 service
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){

        Item item = itemRepository.findById(orderDto.getItemId()) // 주문할 상품을 조회
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원의 회원 조회 (이메일 정보로)

        List<OrderItem> orderItemList = new ArrayList<>();

        // orderItem: 주문 상태 엔티티
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // item: 주문할 상품 엔티티, .getCount(): 주문 수량
        orderItemList.add(orderItem);

        // order: 주문 엔티티
        Order order = Order.createOrder(member, orderItemList); // member: 회원 정보 엔티티, orderItemList: 상품 리스트
        orderRepository.save(order);


        return order.getId(); // 생성한 주문 엔티티의 id 값 리턴!
    }

}
