package kr.co.codewiki.shoppingmall.service;

import kr.co.codewiki.shoppingmall.dto.MemberFormDto;
import kr.co.codewiki.shoppingmall.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMemberTest(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        // 여기까지는 dto

        return Member.createMember(memberFormDto, passwordEncoder); // 여기서 지금 dto -> entity 인 거지
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){

        Member member = createMemberTest(); // member: dto -> entity
        Member savedMember = memberService.saveMember(member);// savedMember: 중복 가입 막는거 + 중복가입 아니면 save


        // 두개 같나 확인하기
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){

        Member member1 = createMemberTest();// member1: dto -> entity
        memberService.saveMember(member1); // member1: 중복 가입 막는거 + 중복가입 아니면 save


        Member member2 = createMemberTest(); // dto -> entity
        Throwable e = assertThrows(IllegalStateException.class, () -> { //  중복 가입 막는거 => 이메일이 같아! fail
            memberService.saveMember(member2); // 다 다른데 이메일이 같아서 오류가 나버렸어!!
        });

        assertEquals("이가회", e.getMessage());

    }
}