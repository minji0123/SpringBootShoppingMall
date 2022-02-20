package kr.co.codewiki.shoppingmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// 회원가입 후 보여줄 메인 페이지

@Controller
public class MainController {

    @GetMapping("/")
    public String main(){
        return "main";
    }

}
