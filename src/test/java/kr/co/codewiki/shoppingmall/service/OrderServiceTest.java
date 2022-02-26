package kr.co.codewiki.shoppingmall.service;

import kr.co.codewiki.shoppingmall.constant.ItemSellStatus;
import kr.co.codewiki.shoppingmall.constant.OrderStatus;
import kr.co.codewiki.shoppingmall.dto.OrderDto;
import kr.co.codewiki.shoppingmall.entity.Item;
import kr.co.codewiki.shoppingmall.entity.Member;
import kr.co.codewiki.shoppingmall.entity.Order;
import kr.co.codewiki.shoppingmall.entity.OrderItem;
import kr.co.codewiki.shoppingmall.repository.ItemRepository;
import kr.co.codewiki.shoppingmall.repository.MemberRepository;
import kr.co.codewiki.shoppingmall.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    // 주문할 상품 저장
    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    // 회원 정보 저장
    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);

    }

    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10); // 주문할 상품 개수 10개로 지정
        orderDto.setItemId(item.getId()); // 지정한 개수 oderDto 에 넣어놓음

        Long orderId = orderService.order(orderDto, member.getEmail());// orderId: 주문 로직 호출 후 생성된 주문 번호
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = orderDto.getCount()*item.getPrice(); // 총 가격

        assertEquals(totalPrice, order.getTotalPrice()); // 총 가격 == db 에 저장된 상품 가격 이면은 성공!
    }


    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10); // 10개 주문 넣음
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId); // 10개 주문 취소

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus()); // cancel == 주문상태
        assertEquals(100, item.getStockNumber()); // 처음개수 == 주문넣다뺐다 한 결과값
    }

}