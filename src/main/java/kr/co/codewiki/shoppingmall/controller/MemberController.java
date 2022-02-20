package kr.co.codewiki.shoppingmall.controller;

import kr.co.codewiki.shoppingmall.dto.MemberFormDto;
import kr.co.codewiki.shoppingmall.entity.Member;
import kr.co.codewiki.shoppingmall.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
// 회원가입 controller

@Controller
@RequiredArgsConstructor // @Autowired 대신
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService; // final 로 해야함
    private final PasswordEncoder passwordEncoder;

    // 회원가입 FORM
    @GetMapping("/new")
    public String memberForm(Model model){

        MemberFormDto memberFormDto = new MemberFormDto();

        model.addAttribute("memberFormDto", memberFormDto);
        return "member/memberForm";
    }

    // 회원가입 성공 시 메인으로 리다이렉트
    // 실패하면 다시 회원가입 페이지로 돌아감
    @PostMapping("/new")
    public String newMember (@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        // Valid 검증하려는 객체(memberFormDto) 앞에 붙임
        // 검증 완료 되면은 결과를 bindingResult(TF) 에다가 담아줌

        //BindingResult는 검증 오류가 발생할 경우 오류 내용을 보관하는 스프링 프레임워크에서 제공하는 객체입니다.
        // bindingResult.addError 를 해줘야 하지만, DTO 에서 어노테이션으로 처리했기 때문에 페이지 리턴만 해주면 된다!!!

        //출처: https://jaimemin.tistory.com/1874 [꾸준함]


        if (bindingResult.hasErrors()){ // bindingResult.hasErrors 를 호출해서, 에러가 있으면 회원가입 페이지로 return 함
            return "member/memberForm";
        }
        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder); // member: entity, createMember 에서 dto-> entity
            memberService.saveMember(member); // saveMember: 중복가입막는거 처리해주고 save

        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage()); // 뷰 페이지에다가 이거 보여주고
            return "member/memberForm"; // 여기로 return 한다고??
        }
        return "redirect:/"; // 다 되면은 리다이렉트???!
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }

    // 로그인 페이지_뭔가 오류났을 때
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비번 확인ㄱ");
        return "member/memberLoginForm";
    }

}
