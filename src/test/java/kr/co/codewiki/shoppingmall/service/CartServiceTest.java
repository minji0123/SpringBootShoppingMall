package kr.co.codewiki.shoppingmall.service;

import kr.co.codewiki.shoppingmall.constant.ItemSellStatus;
import kr.co.codewiki.shoppingmall.dto.CartItemDto;
import kr.co.codewiki.shoppingmall.entity.CartItem;
import kr.co.codewiki.shoppingmall.entity.Item;
import kr.co.codewiki.shoppingmall.entity.Member;
import kr.co.codewiki.shoppingmall.repository.CartItemRepository;
import kr.co.codewiki.shoppingmall.repository.ItemRepository;
import kr.co.codewiki.shoppingmall.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5); // 상품을 장바구니에 5개 담음
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail()); // 장바구니 상품 아이디 (실제 담긴 거)
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new); // 장바구니에 담긴 상품 아이디 (db 조회 값)

        assertEquals(item.getId(), cartItem.getItem().getId()); // 상품 아이디 == 장바구니 안에 있는 상품 아이디
        assertEquals(cartItemDto.getCount(), cartItem.getCount()); // 실제로 담겨있는 개수 == 장바구니 안에 있는 상품 개수
    }

}