package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.OrderStatus;
import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

//    테스트 상품 아이템 설정
    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

//    테스트 유저 설정
    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);

    }


    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
//        주문할 수량 10개
        orderDto.setCount(10);
//        주문할 상품 아이디
        orderDto.setItemId(item.getId());

//        주문 로직 호출 결과 생성된 주문 번호를 orderId에 저장
        Long orderId = orderService.order(orderDto, member.getEmail());
//        주문 번호를 이용해서 저장되어있는 주문 정보를 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();

//        총 가격 계산 
        int totalPrice = orderDto.getCount()*item.getPrice();

//        DB 가격과 같으면 성공
        assertEquals(totalPrice, order.getTotalPrice());
    }

//    @Test
//    @DisplayName("주문 취소 테스트")
//    public void cancelOrder(){
//        Item item = saveItem();
//        Member member = saveMember();
//
//        OrderDto orderDto = new OrderDto();
//        orderDto.setCount(10);
//        orderDto.setItemId(item.getId());
//        Long orderId = orderService.order(orderDto, member.getEmail());
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(EntityNotFoundException::new);
//        orderService.cancelOrder(orderId);
//
//        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
//        assertEquals(100, item.getStockNumber());
//    }

}