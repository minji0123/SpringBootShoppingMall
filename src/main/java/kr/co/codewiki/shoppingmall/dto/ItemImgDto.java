package kr.co.codewiki.shoppingmall.dto;

import kr.co.codewiki.shoppingmall.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    // entity <-> dto 과정이 너무 복잡하고 귀찮아서 이 라이브러리를 사용함.(의존성 추가 해야 함)
    private static ModelMapper modelMapper = new ModelMapper();

    // ModelMapper: 서로 다른 클래스의 값을! 필드의 이름과 자료형이 같을 때! getter, setter 를 통해 값을 복사해서 객체를 반환함
    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);
        // entity 를 파라미터로 받아서 dto 로 반환함
    }

}