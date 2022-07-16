package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
//    정렬할 때 사용하는 order 키워드가 있기 때문에 Order 엔티티에 매핑되는 테이블로 orders 지정.
    @Column(name = "order_id")
    private Long id;

//    한 명의 회원이 여러 번 주문을 할 수 있기 때문에 주문 엔티티 기준 다대일 단방향
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

//    주문일
    private LocalDateTime orderDate;

//    주문상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

//    주문 상품 Entity와 일대다 매핑. mappedBy 속성으로 연관 관계의 주인을 설정.
//    속성 값이 order인 이유는 OrderItem에 있는 Order에 의해 관리된다는 의미
//    부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeTypeAll 옵션을 설정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    하나의 주문이 여러 개의 주문 상품을 가진다.
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
