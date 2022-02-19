package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.constant.ItemSellStatus;
import kr.co.codewiki.shoppingmall.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){

        //        Item item = new Item();
//        item.setItemNm("테스트 상품");
//        item.setPrice(10000);
//        item.setItemDetail("테스트 상품 상세 설명");
//        item.setItemSellStatus(ItemSellStatus.SELL); // enum 은 new 안해도 되는 듯?!?
//        item.setStockNum(100);
//        item.setRegTime(LocalDateTime.now());
//        item.setUpdateTime(LocalDateTime.now());
//        Item savedItem = itemRepository.save(item);
//        System.out.println(savedItem.toString());


        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .itemDetail("테스트 상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNum(100)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());

    }

    public void createItemList(){
        for(int i=1; i<=10; i++){
            Item item = Item.builder() // static 함수
                    .itemNm("테스트 상품" +i)
                    .price(10000+i)
                    .itemDetail("테스트 상품 상세 설명"+i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNum(100)
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList ){
            System.out.println("findByItemNmTest: "+item.toString());
        }
        System.out.println("");
    }

    @Test
    @DisplayName("상품명, 상품상세설명 조회 테스트")
    public void findByItemNmOrItemDetailTest(){
        createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for (Item item : itemList ){
            System.out.println("[findByItemNmOrItemDetailTest]: "+item.toString());
        }
        System.out.println("");
    }

    @Test
    @DisplayName("가격 범위 조회 테스트_lessThan")
    public void findByPriceLessThanTest(){
        createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10003);
        for (Item item : itemList ){
            System.out.println("[findByPriceLessThanTest]: "+item.toString());
        }
        System.out.println("");
    }

    @Test
    @DisplayName("가격 내림차순 정렬")
    public void findAllByOrderByPriceDescTest(){
        createItemList();
        List<Item> itemList = itemRepository.findAllByOrderByPriceDesc();
        for (Item item : itemList ){
            System.out.println("[findAllByOrderByPriceDescTest]: "+item.toString());
        }
        System.out.println("");
    }

    // JPQL
    @Test
    @DisplayName("@Query 를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList ){
            System.out.println("[findByItemDetailTest]: "+item.toString());
        }
        System.out.println("");
    }


//    // JPQL Querydsl
//    @PersistenceContext
//    EntityManager em;
//
//    @Test
//    @DisplayName("Querydsl 조회 테스트1")
//    public void queryDslTest(){
//        createItemList();
//        JPAQuery
//    }




}