package kr.co.codewiki.shoppingmall.service;

import kr.co.codewiki.shoppingmall.dto.OrderDto; 
import kr.co.codewiki.shoppingmall.dto.OrderHistDto;
import kr.co.codewiki.shoppingmall.dto.OrderItemDto;
import kr.co.codewiki.shoppingmall.entity.*; 
import kr.co.codewiki.shoppingmall.repository.ItemImgRepository;
import kr.co.codewiki.shoppingmall.repository.ItemRepository;
import kr.co.codewiki.shoppingmall.repository.MemberRepository;
import kr.co.codewiki.shoppingmall.repository.OrderRepository;
import lombok.RequiredArgsConstructor; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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


    // 주문을 위한 로직 
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


    // 주문 목록을 조회하기 위한 로직
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable); // 주문 목록을 조회
        Long totalCount = orderRepository.countOrder(email); // 주문 총 개수

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) { // entity -> dto

                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y"); // 대표상품인지 보는거 (상품 이력 페이지에 출력해야 하니까)
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl()); // entity-> dto
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }


    // DB 에 있는 email 과 주문자 email 대조함
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email); // 유저 이메일 받아옴
        Order order = orderRepository.findById(orderId) // 주문 데이터 받아오고
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember(); // 위의 주문을 한 유저의 정보를 받아옴

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false; // 그 둘이 같지 않으면은 false
        }

        return true; // 같으면 true
    }

    // 주문 취소하는 로직 구현 service
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    // 장바구니에서 주문할 상품 데이터를 전달받아서 주문 생성
    public Long orders(List<OrderDto> orderDtoList, String email){

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        // 주문할 상품 리스트
        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem); // 밑에 담아줌
        }

        Order order = Order.createOrder(member, orderItemList); // 회원이랑 주문할 상품 리스트들을 주문에 담음
        orderRepository.save(order);

        return order.getId();
    }

}
