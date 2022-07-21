package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

// 카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 담겨있는지 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

//    장바구니에 담겨져있는 장바구니 번호와 지금 장바구니의 번호, 아이템 번호, 대표 사진이 있다면 
//    장바구니 등록 시간을 기준으로 내림차순 한다.
//    즉, 제일 최근의 장바구니부터 보이게 되는 것
    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}