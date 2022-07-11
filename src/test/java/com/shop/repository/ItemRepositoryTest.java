package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.thymeleaf.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

//통합 테스트를 위해 스프링부트에서 제공하는 어노테이션
@SpringBootTest
//테스트 코드 실행 시 H2 데이터베이스를 사용하게 한다
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {
//    이 어노테이션을 이용하여 Bean 주입
    @Autowired
    ItemRepository itemRepository;
//    영속성 컨텍스트 사용하기 위한 Annotation
    @PersistenceContext
    EntityManager em;
    
//    테스트할 메소드 위에 선언하여 해당 메소드를 테스트 대상으로 지정
    @Test
//    Junit5에 추가된 어노테이션. 테스트 코드 실행 시 여기에서 지정한 테스트명이 노출된다.
    @DisplayName("상품저장테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
//      작성한 내용 저장.
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

//    임의의 상품 데이터 10개 추가
    public void createItemList(){
        for (int i=1; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
//      작성한 내용 저장.
            Item savedItem = itemRepository.save(item);
        }
    }


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
//       상품 리스트 먼저 만들고
        this.createItemTest();
//        그 다음 조회
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1","테스트 제품 상세 설명5");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Query를 이용한 상품조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery를 이용한 상품 조회 테스트")
    public void findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createItemList();
//        동적 쿼리 생성
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//       플러그인을 통해 자동으로 생성된 QItem 객체 이용
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

//       JPAQuery 메소드 fetch를 이용하여 쿼리 결과를 리스트로 반환.
        List<Item> itemList = query.fetch();
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    public void createItemList2(){
        for (int i=1; i<=5; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
//      작성한 내용 저장.
            Item savedItem = itemRepository.save(item);
        }
        for (int i=6; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
//      작성한 내용 저장.
            Item savedItem = itemRepository.save(item);
        }
    }
    @Test
    @DisplayName("Querydsl 조회 테스트2")
    public void queryDslTest2() {
        this.createItemList2();

//쿼리에 들어갈 조건을 만들어주는 빌더. Predicate를 구현하고 있고, 메소드 체인 형식으로 사용함
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";
        
//       상품의 판매 상태가 SELL일 때만 판매 상태 조건을 동적으로 추가
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPageResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements" + itemPageResult.getTotalElements());
    }
}