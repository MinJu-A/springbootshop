package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
//상품 등록페이지에 접근할 수 있도록 Controller 클래스 작성
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){
//        필수 값이 없다면 다시 상품 등록 페이지로 전환
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
//            첫 번째 이미지가 없다면 에러 메세지와 함께 상품 등록 페이지로 전환.
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다. ");
            return "item/itemForm";
        }

        try{
//            상품 저장 로직 호출.
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
//        정상적으로 상품 등록 시 메인 페이지로 이동
        return "redirect:/";
    }
}
