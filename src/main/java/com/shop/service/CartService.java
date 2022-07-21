package com.shop.service;

import com.shop.dto.CartItemDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email){

//        장바구니에 담을 상품 entity 조회
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
//        현재 로그인 한 회원 엔티티 조회
        Member member = memberRepository.findByEmail(email);

//        현재 로그인 한 회원의 장바구니 아이템 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
//        장바구니 없으면 새 장바구니 생성 
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

//        현재 상품이 장바구니에 들어가있는지 체크
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

//        장바구니에 원래 있던 상품이면
        if(savedCartItem != null){
//            기존 수량에 현재 장바구니에 담을 수량 추가
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
//            원래 있던 상춤이 아니면 새로 만들어요
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    
}