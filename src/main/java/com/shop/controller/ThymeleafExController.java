package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
// "/thymeleaf" 경로로 요청온 걸 매핑해준다.
@RequestMapping(value = "/thymeleaf")

public class ThymeleafExController {
    @GetMapping(value = "/ex01")
//    model 객테를 이용하여 뷰에 전달한 데이터를 key, value 구조로 넣어준다.
    public String thymeleafExample01(Model model){
        model.addAttribute("data" ,"타임리프 예제입니다.");
//        tmeplates 폴더 기준으로 뷰의 위치와 이름을 반환
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value = "/ex02")
    public String thymeleafExample02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();


//        10개의 테스트 데이터 만들기
        for (int i=1; i<=10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(10000 + i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }


        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();


//        10개의 테스트 데이터 만들기
        for (int i=1; i<=10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(10000 + i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }


        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }


    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
//    전달한 매개 변수와 같은 이름/타입의 변수를 파라미터로 설정하면 데이터가 자동 바인딩 된다.
    public String thymeleafExample06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
//    전달한 매개 변수와 같은 이름/타입의 변수를 파라미터로 설정하면 데이터가 자동 바인딩 된다.
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }

}
