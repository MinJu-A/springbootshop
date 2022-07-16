package com.shop.entity;


import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import  static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "calsspath:application-test.properties")
@Transactional
 class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품");
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTesT(){

        Order order = new Order();

        for (int i=0; i<3; i++){
            Item item = new Item();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
//            아직 영속성 컨텍스트에 담기지 않은 orderItem Entity를 order Entity에 담아준다.
            order.getOrderItems().add(orderItem);
        }

//        order Entity를 저장하면서 강제로 flush를 호출. 영속성 컨텍스트에 있는 객체들을 DB에 반영.
        orderRepository.saveAndFlush(order);
//        영속성 컨텍스트 상태 초기화
        em.clear();

//        DB에서 주문 Entity를 조회한다. select 쿼리가 실행된다.
        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
//        실제로 데이터가 저장되었는지 검사
        assertEquals(3, savedOrder.getOrderItems().size());
    }
}
