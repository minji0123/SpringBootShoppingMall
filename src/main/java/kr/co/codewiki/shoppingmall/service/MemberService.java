package kr.co.codewiki.shoppingmall.service;

import kr.co.codewiki.shoppingmall.entity.Member;
import kr.co.codewiki.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 롤백!
@RequiredArgsConstructor // @AutoWired 없이 new 해주기
public class MemberService implements UserDetailsService {

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

    // 로그인 로그아웃 구현!
    @Override  // loadUserByUsername 오버라이딩
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // user 의 이메일을 전달받는다.(중복ㄴㄴ인애)
        Member member = memberRepository.findByEmail(email);

        if (member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString()) // enum 이니까 toString 해준다.
                .build();
    }
}
