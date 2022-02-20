package kr.co.codewiki.shoppingmall.entity;


import kr.co.codewiki.shoppingmall.dto.MemberFormDto;
import kr.co.codewiki.shoppingmall.repository.CartRepository;
import kr.co.codewiki.shoppingmall.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class CartTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em; // 아줌마
        // 엔티티는 과자, 매니저는 아줌마, 팩토리는 공장
        // 영속성 컨텍스트: 엔티티를 영구히 저장하는 환경 _램! (영속성 컨텍스트를 관리하는 모든 엔티티 매니저가 초기화 및 종료되지 않는 한)

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }


    // 영속성: 기본적으로 컴퓨터 공학에서 영속성이라고 하면 비휘발성

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); // 아줌마가 과자를 db에 집어넣어주고
        em.clear(); // 아줌마가 과자를 db 로부터 비워준다. 왜 비워주냐면 db 에서 장바구니 엔티티를 가지고 올 때 회원 엔티티도 같이 가져오는지

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getId(), member.getId());
    }
}