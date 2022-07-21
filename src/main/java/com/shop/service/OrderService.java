package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

//        로그인 한 회원의 이메일 정보를 이용하여 회원 정보 조회
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
//        주문할 상품 entity와 주문 수량을 이용하여 주문 상품 entity생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

//        회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티 생성
        Order order = Order.createOrder(member, orderItemList);
//        생성한 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

//        유저의 이메일(아이디)와 페이징 조건을 이용하여 주문 목록 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);
//        주문 총 개수
        Long totalCount = orderRepository.countOrder(email);

//        구매 이력에 전달할 dto 생성
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

//        order가 지금 로그인 한 유저의 이메일이라면 주문한 상품의 대표 이미지를 조회한다.
        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }
//        페이지 구현 객체를 사용하여 리턴
        return new PageImpl<>(orderHistDtos, pageable, totalCount);
    }
}