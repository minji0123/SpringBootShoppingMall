package kr.co.codewiki.shoppingmall.service;

import kr.co.codewiki.shoppingmall.entity.Member;
import kr.co.codewiki.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 롤백!
@RequiredArgsConstructor // @AutoWired 없이 new 해주기
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){ // 중복 가입 막는거 + 중복가입 아니면 save
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail()); // 이메일 체크하고_repository 에 있는 메소드로로
        if(findMember != null){
            throw new IllegalStateException("이가회"); // IllegalStateException: 이미 가입된 회원이면 메세지 날림
        }
    }

}
