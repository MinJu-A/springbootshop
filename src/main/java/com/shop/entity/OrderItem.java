package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
//BaseEntity 상속 받음
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

//    주문 상품 기준 다대일 단방향 매핑
//    지연 로딩 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

//    한 번의 주문에 여러 개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 주문 엔티티를 다대일 단반향 매핑으로 설정
//    지연 로딩 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

//    주문 가격
    private int orderPrice;

//    수량
    private int count;

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
//        현재 시간 기준 상품 가격을 주문 가격으로 세팅
        orderItem.setOrderPrice(item.getPrice());

//        주문 수량만큼 상품의 재고 수량 감소
        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){

        return orderPrice * count;
    }

}
