package kr.co.codewiki.shoppingmall.controller;

import kr.co.codewiki.shoppingmall.dto.ItemSearchDto;
import kr.co.codewiki.shoppingmall.dto.MainItemDto;
import kr.co.codewiki.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
// 회원가입 후 보여줄 메인 페이지

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    // 메인 페이지에 상품 데이터를 보여줄 get 페이지
    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6); // 처음 페이지(0) 을 보여주고, 한 페이지에 6개 보여줄거임
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

}