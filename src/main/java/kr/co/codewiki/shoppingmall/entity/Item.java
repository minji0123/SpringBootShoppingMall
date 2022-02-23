package kr.co.codewiki.shoppingmall.entity;

import kr.co.codewiki.shoppingmall.constant.ItemSellStatus;
import kr.co.codewiki.shoppingmall.dto.ItemFormDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item extends BaseEntity{

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Column()
    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

//    @Column(name="price")
    @Column(name="price", nullable = false)
    private int price;

//    @Column
    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable = false)
//    @Column
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태


    // 상품 데이터를 업데이트 하는 로직 생성
    public void updateItem(ItemFormDto itemFormDto){ //
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
    /*
    엔티티 클래스에 비즈니스 로직을 추가하면 객체지향적으로 코딩을 할 수 있고, 코드를 재활용 할 수도 있다.
    데이터 변경 포인트를 여기로 지정할 수 있움(데이터 수정 변경 여기서만 한다는 뜻)
     */

}
