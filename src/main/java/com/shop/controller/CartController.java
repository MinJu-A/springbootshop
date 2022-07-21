package com.shop.controller;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.service.CartService;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){

//        상품 정보를 받아오는 DTO가 바인딩 에러가 있는지 체크
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

//        조회할 이메일은 현재 로그인 되어있는 사용자의 이메일로 설정
        String email = principal.getName();
        Long cartItemId;

        try {
//            현재 로그인 한 회원의 이메일 정보를 이용하여 장바구니에 상품을 담는다.
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){
//        현재 로그인 한 사용자의 이메일 정보를 이용하여 사용자의 장바구니에 담겨져있는 상품 정보 조회
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailList);
        return "cart/cartList";
    }

//    장바구니의 수량을 업데이트 하는 요청
    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){

//        업데이트니까 0개 이하로 요청이 들어올 경우 에러 메세지 리턴
        if(count <= 0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
//            로그인 한 사용자가 장바구니를 등록한 사용자가 맞는지 수정 권한 체크 
        } else if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

//        권한이 있다면 장바구니 상품의 개수를 업데이트
        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }


//   자원을 삭제할 때 쓰는 DeleteMapping Annotation 사용
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

//        지금 로그인 된 사용자와 장바구니를 올린 사용자가 같은지 권한 체크
        if(!cartService.validateCartItem(cartItemId, principal.getName())){

//            권한이 없으면 에러 메세지 리턴
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

//        권한이 있다면 삭제하는 메소드 호출
        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

//    장바구니 안에 있는 상품을 주문한다
    @PostMapping(value = "/cart/orders")
//    장바구니에 있는 정보를 가지고 와요
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

//        장바구니에서 주문할 리스트가 비어있거나 선택 된 상품이 없다면 에러 메세지 리턴
        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

//        주문이 완료된 후 주문 번호 생성
        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
//        생성된 주문 번호와 함께 성공 상태 코드를 받는다
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}