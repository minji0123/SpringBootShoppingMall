package kr.co.codewiki.shoppingmall.controller;

import kr.co.codewiki.shoppingmall.dto.OrderDto;
import kr.co.codewiki.shoppingmall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

// 주문 관련 요청들을 처리
// 비동기 방식 사용_상품 주문에서 웹 페이지의 새로 고침 없이 서버에서 주문을 요청하기 위해서
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order") // @ResponseBody, @RequestBody: 비동기 처리를 할 때 사용
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal){
        // @ResponseBody: Http 요청의 본문 body 에 담긴 내용을 자바 객체로 전달
        // @RequestBody: 자바 객체를 HTTP 요청의 body 로 전달
        // Principal: 파라미터로 넘어온 principal 객체에 데이터를 넣으면, 해당 객체에 직접 접근할 수 있다. (@Controller 가 선언되어있어야함)

        // 주문 정보를 받는 orderDto 객체에 데이터 바인딩 시 에러가 있는지 검사
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
            // 에러 정보는 ResponseEntity 객체에 담겨져서 전달됨. 그니까 에러정보는 entity
        }


        // 현재 로그인한 회원의 이메일 정보 (로그인 유저 정보 얻을려고)
        String email = principal.getName(); // principal 객체에서 현재 로그인한 회원의 이메일 정보를 조회함
        Long orderId;

        try { //주문 로직 호출!!!
            orderId = orderService.order(orderDto, email); // orderDto: 화면으로 넘어온 주문 정보, email: 회원 이메일 정보
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK); // http 응답 코드를 반환!
    }

}
