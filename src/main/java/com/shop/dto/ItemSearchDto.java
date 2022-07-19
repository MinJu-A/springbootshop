package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

//    현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회한다.
//    all : 상품 등록일 전체, 1d: 최근 하루동안 등록된 상품, 1w:최근 일주일동안 등록된 상품, 1m:최근한달, 6m:최근 6개월
    private String searchDateType;

//    판매 상태를 기준으로 데이터 조회
    private ItemSellStatus searchSellStatus;

//   상품을 어떤 유형으로 조회할지 선택
//    itemNm : 상품명, createBy : 등록자ID
    private String searchBy;

//    조회할 검색어를 저장할 변수
    private String searchQuery = "";

}