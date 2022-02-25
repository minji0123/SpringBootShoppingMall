package kr.co.codewiki.shoppingmall.entity;

import kr.co.codewiki.shoppingmall.constant.ItemSellStatus;
import kr.co.codewiki.shoppingmall.dto.ItemFormDto;
import kr.co.codewiki.shoppingmall.exception.OutOfStockException;
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
    엔티티 클래스에 비즈니스 로직을 추가하면
    1. 객체지향적으로 코딩을 할 수 있고,
    2. 코드를 재활용 할 수도 있다.
    3. 데이터 변경 포인트를 여기로 지정할 수 있움(데이터 수정 변경 여기서만 한다는 뜻)
     */

    // 상품 주문 -> 상품 재고 감소 로직 생성
    public void removeStock(int stockNumber){

        int restStock = this.stockNumber - stockNumber; // stockNumber: 상품의 재고 수량 restStock: 주문 후 남은 재고 수량

        if(restStock<0){ // 상품 재고가 주문 수량보다 작을 경우, 재고 부족 예외 발생
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock; // 주문 후 남은 재고 수량 = 상품의 현재 재고 값
    }

//    public void addStock(int stockNumber){
//        this.stockNumber += stockNumber;
//    }


}
