package kr.co.codewiki.shoppingmall.controller;

import kr.co.codewiki.shoppingmall.dto.ItemFormDto;
import kr.co.codewiki.shoppingmall.dto.ItemSearchDto;
import kr.co.codewiki.shoppingmall.entity.Item;
import kr.co.codewiki.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


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

    // 상품 수정 get 페이지
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto); // 조회한 상품 데이터를 model 에 담아서 뷰로 전달함
        }

        catch(EntityNotFoundException e){ // 상품 엔티티가 존재하지 않으면은 에러메세지 + 상품 등록페이지로 다시 ㄱㄱ
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    // 상품 수정 post
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){

        // 상품 수정 시 필수 값이 없을 때 애러 발생
        if(bindingResult.hasErrors()){
            return "item/itemForm"; // 에러가 발생하면 상품 수정 get 페이지로 이동
        }

        // 상품 수정 시 첫번째 이미지가 없으면 애러 발생 (첫 번째 이미지는 대표 상품 이미지여서 꼭 있어야함!)
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";// 에러가 발생하면 상품 수정 get 페이지로 이동
        }

        try {// 상품 수정 로직 호출
            itemService.updateItem(itemFormDto, itemImgFileList); // itemFormDto: 상품 정보, itemImgFileList: 상품 이미지 정보들 리스트
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/"; // 메인 페이지로 리다이렉트
    }

    // 상품 관리 화면 get 페이지
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"}) // url 에 페이지 번호가 없는거랑, 페이지 번호가 있는거 둘 다 매핑해줌
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        // 한 페이지 당 3개만 보여줄거임
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        // 0: 조회할 페이지 번호, 3: 한 번에 가지고 올 데이터 수
        // url 에 페이지 번호가 있으면은 그 페이지를 보여주고, url 에 번호가 없으면 0 페이지 보여줌

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable); // itemSearchDto: 조회 조건 pageable: 페이징 정보

        model.addAttribute("items", items); // item: 조회한 상품 데이터
        model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있게 뷰에 전달
        model.addAttribute("maxPage", 5); // 최대 5개의 이동할 페이지 번호를 보여줌줌

       return "item/itemMng"; // 조회한 상품 데이터 전달받는 페이지
    }

//    @GetMapping(value = "/item/{itemId}")
//    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
//        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
//        model.addAttribute("item", itemFormDto);
//        return "item/itemDtl";
//    }


}
