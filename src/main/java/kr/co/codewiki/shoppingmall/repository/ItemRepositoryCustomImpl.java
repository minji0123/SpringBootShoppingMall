package kr.co.codewiki.shoppingmall.repository;

// BooleanExpression: where 절에서 사용할 수 있는 값을 지원해줌
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.codewiki.shoppingmall.constant.ItemSellStatus;
import kr.co.codewiki.shoppingmall.dto.ItemSearchDto;
import kr.co.codewiki.shoppingmall.dto.MainItemDto;
import kr.co.codewiki.shoppingmall.dto.QMainItemDto;
import kr.co.codewiki.shoppingmall.entity.Item;
import kr.co.codewiki.shoppingmall.entity.QItem;
import kr.co.codewiki.shoppingmall.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
/*
 * Querydsl 을 SpringDataJpa 와 함께 사용하기 위해서는 사용자 정의 repository 를 작성해야 함
 * 사용자 정의 repository 는 총 3단계임
 * 1. 사용자 정의 인터페이스 작성
 * 2. 사용자 정의 인터페이스 구현 ***
 * 3. Spring Data Jpa repository 에서 사용자 정의 인터페이스 상속 (ItemRepository)
 * */

//2. 사용자 정의 인터페이스 구현 (클래스 뒤에 Impl 을 꼭 붙여야 한다.)
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory; // JPAQueryFactory 클래스: 동적으로 쿼리를 생성해줌

    public ItemRepositoryCustomImpl(EntityManager em){ // EntityManager: JPAQueryFactory 의 생성자로 많이 사용함
        this.queryFactory = new JPAQueryFactory(em);
    }


    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        // 상품 판매 조건이 전체일 경우, null 리턴 (이러면은 where 절에서 조건이 무시됨)
        // 상품 판매 조건이 판매중 or 품절일 경우, where 조건 활성화
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        // searchDateType 에 따라서 dateTime 의 값을 세팅 후, 그 세팅한 시간 이후로 등록된 상품만 조회함
        // 그니까 searchDateType 1m 이면은 dateTime 가 1달 전으로 세팅되고 상품 조회함

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        // searchBy 값에 따라 (where 조건으로) 상품을 조회 하도록 조건값을 반환함

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }


    // queryFactory 로 쿼리 동적 생성
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery())) // , 는 and 조건임
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()) // 데이터를 가지고 올 시작 인덱스를 지정
                .limit(pageable.getPageSize()) // 한 번에 가지고 올 최대 개수 지정
                .fetchResults(); //  QueryResults 를 반환 (상품 데이터 리스트 조회 , 상품 데이터 전체 개수 조회함)

        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total); // 조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환 (?)
    }

    // item like 검색 (밑에서 쓰려구)
    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    // queryFactory 로 쿼리 동적 생성
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(
                        new QMainItemDto( // 원래는 entity 로 변환해줘야 하는데, mainitemdto 의 어노테이션 (QueryProjection)덕분에 dto 로 그냥 사용
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item) // 내부 조인
                .where(itemImg.repimgYn.eq("Y")) // 대표 상품인 경우에는 Y
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    /*
    * Querydsl 결과 조회 메소드
    * 1. QueryResults<T> fetchResults(): 조회 대상 리스트 및 전체 개수 return (QueryResults)_위에서 사용함
    * 2. List<T> fetch(): 조회 대상 리스트 반환
    * 3. T fetchOne(): 조회 대상이 1건이면 해당 타입 반환, 1개 이상이면 애러 반환
    * 4. T fetchFirst(): 조회 대상이 1건 or 1건 이상이면 1건만 반환
    * 5. long fetchCount(): 해당 데이터 전체 개수 반환, count 쿼리 실행
    * */



}
