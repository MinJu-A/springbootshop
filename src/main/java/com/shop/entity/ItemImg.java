package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item_img")
@Getter @Setter
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    이미지 파일명
    private String imgName;

//    원본 이미지 파일명
    private String oriImgName;

//    이미지 조회 정보
    private String imgUrl;

//    대표이미지 여부
    private String repimgYn;

//    지연 로딩 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

//    파일명, 업데이트 할 이미지 파일명, 이미지 경로를 파라미터로 입력 받아서 이미지 정보 업데이트
    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }


}
