package kr.co.codewiki.shoppingmall.repository;

import kr.co.codewiki.shoppingmall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
/*
 * Querydsl 을 SpringDataJpa 와 함께 사용하기 위해서는 사용자 정의 repository 를 작성해야 함
 * 사용자 정의 repository 는 총 3단계임
 * 1. 사용자 정의 인터페이스 작성
 * 2. 사용자 정의 인터페이스 구현
 * 3. Spring Data Jpa repository 에서 사용자 정의 인터페이스 상속 ** (ItemRepository)
 * */

// 3. ItemRepository 에서 ItemRepositoryCustom 상속
public interface ItemRepository extends JpaRepository<Item,Long> , QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    // 쿼리 메소드: find + (엔티티이름) + By+ 변수이름
    // 조건 하나 추가할 때 마다 By 를 넣어줘야 한다!!!
    /*
    * 쿼리 메소드는 조건이 많아지면
    * 쿼리 메소드의 이름을 보고 어떻게 동작하는지 해석하기 어렵다.
    * */

    // 상품명으로 데이터 조회하기
    List<Item> findByItemNm(String itemNm);

    // or 조건
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    // lessThan 조건
    List<Item> findByPriceLessThan(Integer price);

    // orderBy 조건
    List<Item> findAllByOrderByPriceDesc();

    // orderBy 조건 + 가격 조건
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);



    // JPQL
    // 쿼리 어노테이션

    // 특정 문자가 포함된 아이템 설명 + 가격 내림차순 검색
    // select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc


    // nativeQuery
    // 기존의 db 에서 사용하던 쿼리를 그대로 사용해야 할 때 복잡한 쿼리를 그대로 사용 가능
    // 얘는 데이터베이스 종속적임임
   @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);


    // JPQL Querydsl
    // 동적으로 쿼리 생성 가능
    // 자바 소스코드로 쿼리를 작성하기 때문에 컴파일 시점에서 오류를 발견할 수 있음
}
/*
* <S extends T> save (S entity): 엔티티 저장 및 수정
* void delete(T entity): 엔티티 삭제
* count(): 엔티티 총 개수 반환
* Iterable<T> findAll(): 모든 엔티티 조회
* */