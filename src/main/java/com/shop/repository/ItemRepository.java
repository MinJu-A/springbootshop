package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//JPA repository는 2개의 제네릭 타입 사용. <Entity 타입 클래스, 기본키 타입>
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
//    상품명으로 DB 조회
    List<Item> findByItemNm(String itemNm);
//    Or조건으로 DB 조회
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
//    LenssThan 조건으로 DB 조회
    List<Item> findByPriceLessThan(Integer itemNm);
//    OrderBy 내림차순 조건으로 DB 조회
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer itemNm);

//    @Query를 이용한 검색 처리.
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
//    @Param 어노테이션을 이용해 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정. %:itemDetail% 이게 넘어올 거임
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
//    @Param 어노테이션을 이용해 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정. %:itemDetail% 이게 넘어올 거임
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);


}

