package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{

//    상품 코드
    @Id //기본키 설정
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    상품명
    @Column(nullable = false,length = 50)
    private String itemNm;

//    가격
    @Column(name = "price", nullable = false)
    private int price;

//    재고수량
    @Column(nullable = false)
    private int stockNumber;

//    제품상세설명
    @Lob
    @Column(nullable = false)
    private String itemDetail;

//    상품 판매 상태
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    //    등록 시간
    private LocalDateTime regTime;

    //    수정 시간
    private LocalDateTime updateTime;

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
//        재고 수량에서 주문 후 남은 재고 수량을 구한다. 
        int restStock = this.stockNumber - stockNumber;
//        재고가 주문 수량보다 작을 경우 부족하다는 메세지를 띄운다
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
//        주문 후 남은 재고 수량을 현재 재고 값으로 설정.
        this.stockNumber = restStock;
    }
}
