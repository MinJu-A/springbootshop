package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
//    스프링에서 비동기 처리를 할 때 @RequestBody, @ResponseBody 어노테이션을 사용한다.
//    RequestBody : http 요청의 본문 body에 담긴 내용을 자바 객체로 전달
//    ResponseBody : 자바 객체를 http 요청의 body로 전달 
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto
            , BindingResult bindingResult, Principal principal){

//        http 요청의 본문 body에 담긴 주문 정보를 받아오는 orderDto 객체에 바인딩 에러가 있는지 검사
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

//            에러 정보를 ResponseEntity에 담아서 리턴한다
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

//        현재 로그인 된(인증된) 유저 정보를 얻어온다.
        String email = principal.getName();
        Long orderId;

        try {
//            프론트에서 넘어오는 주문 정보와 회원 정보를 백에서 받아서 주문 로직을 호출
            orderId = orderService.order(orderDto, email);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

//        결과값으로 생성된 주문 번호와 요청이 성공했다는 HTTP 응답 상태 코드를 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){

//        한 번에 가지고 올 수 있는 주문의 갯수를 4개로 설정
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
//        현재 로그인 한 회원의 정보를 가져와서 이 회원의 이메일과 페이징 정보를 리턴
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

}