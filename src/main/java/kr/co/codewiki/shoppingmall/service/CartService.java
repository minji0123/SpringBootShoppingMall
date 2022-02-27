package kr.co.codewiki.shoppingmall.service;

import kr.co.codewiki.shoppingmall.dto.CartItemDto;
import kr.co.codewiki.shoppingmall.entity.Cart;
import kr.co.codewiki.shoppingmall.entity.CartItem;
import kr.co.codewiki.shoppingmall.entity.Item;
import kr.co.codewiki.shoppingmall.entity.Member;
import kr.co.codewiki.shoppingmall.repository.CartItemRepository;
import kr.co.codewiki.shoppingmall.repository.CartRepository;
import kr.co.codewiki.shoppingmall.repository.ItemRepository;
import kr.co.codewiki.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    // 장바구니에 상품을 담는 로직
    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId()) //장바구니에 담을 상품 엔티티 조회
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원 엔티티 조회

        Cart cart = cartRepository.findByMemberId(member.getId()); // 현재 로그인한 회원의 장바구니 엔티티 조회

        if(cart == null){ // 회원에게 장바구니가 없으면, 만들어줌
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 상품이 장바구니에 들어가있는지 아닌지 조회
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 만약 상품이 이미 있으면은 개수를 +
        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else { // 아니면은 CartItem 에 상품 저장
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }
}
