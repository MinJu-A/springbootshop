package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter@Setter
@Table(name = "cart_item")
public class CartItem {

    @Id
//    auto indexing
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

//  하나의 장바구니에는 여러 개의 상품을 담을 수 있기 때문에 다대일.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

//    하나의 상품은 여러 장바구니 상품으로 담길 수 있기 때문에 다대일.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
//    장바구니의 담을 상품의 정보를 담기 위함.
    private Item item;

//    같은 장바구니에 몇 개 담을 건지.
    private int count;
    
}
