package kr.co.codewiki.shoppingmall.controller;

import kr.co.codewiki.shoppingmall.dto.ItemFormDto;
import kr.co.codewiki.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록 get 페이지
    @GetMapping("/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }


    // 상품 등록 post
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        // 상품 등록 시 필수 값이 없을 때 애러 발생
        if(bindingResult.hasErrors()){
            return "item/itemForm"; // 에러가 발생하면 상품 등록 get 페이지로 이동
        }

        // 상품 등록 시 첫번째 이미지가 없으면 애러 발생 (첫 번째 이미지는 대표 상품 이미지여서 꼭 있어야함!)
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){

            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";// 에러가 발생하면 상품 등록 get 페이지로 이동
        }


        try { // 상품 저장 로직 호출
            itemService.saveItem(itemFormDto, itemImgFileList); // itemFormDto: 상품 정보, itemImgFileList: 상품 이미지 정보들 리스트
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/"; // 메인 페이지로 리다이렉트
    }

}
