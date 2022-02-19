package kr.co.codewiki.shoppingmall.controller;

import kr.co.codewiki.shoppingmall.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf") // 클라이언트의 요청에 대해서 어떤 컨트롤러가 처리할지 매핑하는 어노테이션 (/thymeleaf 로 오는 요청을 controller 가 처리하도록 해줌)
public class ThymeleafExController {

    @GetMapping("/ex02")
    public String thymeleafExample02(Model model){ // model: view 에 데이터를 넘길 때 사용
        ItemDto itemDto = ItemDto.builder()
                .itemDetail("상품 상세 설명")
                .itemNm("테스트 상품1")
                .price(10000)
                .regTime(LocalDateTime.now())
                .build();

        model.addAttribute("itemDto",itemDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafExample03(Model model){ // model: view 에 데이터를 넘길 때 사용

        List<ItemDto> itemDtos = new ArrayList<>();

        for (int i=1; i<=10; i++){
            ItemDto itemDto = ItemDto.builder()
                    .itemDetail("상품 상세 설명"+i)
                    .itemNm("테스트 상품"+i)
                    .price(1000*i)
                    .regTime(LocalDateTime.now())
                    .build();

            itemDtos.add(itemDto);
        }

        model.addAttribute("itemDtos",itemDtos);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }
}
