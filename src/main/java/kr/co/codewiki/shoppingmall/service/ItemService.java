package kr.co.codewiki.shoppingmall.service;

// 상품 등록

import kr.co.codewiki.shoppingmall.dto.ItemFormDto;
import kr.co.codewiki.shoppingmall.dto.ItemImgDto;
import kr.co.codewiki.shoppingmall.entity.Item;
import kr.co.codewiki.shoppingmall.entity.ItemImg;
import kr.co.codewiki.shoppingmall.repository.ItemImgRepository;
import kr.co.codewiki.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 상품 등록 service
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y"); // 첫번째 이미지를 대표 상품 이미지로 설정
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 리스트 형태로 이미지들 저장
        }

        return item.getId();
    }

    // 상품 수정하기를 위한 service
    // 상품이랑, 상품이미지의 entity -> dto 로
    @Transactional(readOnly = true) // 트랜젝션을 readOnly 로 설정할 경우, JPA 가 변경감지(더티체킹)를 수행하지 않아서 성능 향상됨
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto); // entity -> dto
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto; //itemFormDto 에는 상품이랑 상품 이미지 다 있움
    }


    // 상품 데이터 수정
    // dto 로 변환된 상품이랑 상품이미지를 수정
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()) // 상품 아이디로 상품 엔티티를 조회 (아이디는 상품 등록 화면에서 전달 받음)
                .orElseThrow(EntityNotFoundException::new);

        item.updateItem(itemFormDto); // itemFormDto 로 상품 엔티티 수정 (itemFormDto 는 상품 등록 화면에서 전달 받음)

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); // 상품 이미지 아이디 리스트 조회

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
            // updateItemImg() 에다가 상품 이미지 id, 이미지 파일 정보를 파라미터를 받고
            // 그걸로 이미지 수정함함
        }

        return item.getId();
    }
}
