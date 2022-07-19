package com.shop.service;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

//    application.properties 파일에 등록한 itemImgLocation 값을 저장
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

//        파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
//            저장 경로, 파일 이름, 파일의 바이트 배열을 파라미터로 하여 uploadFile 메소드를 호출, 그리고 imgName변수에 파일 이름 저장
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

//        상품 이미지 정보 저장
//        imgName : 실제 로컬에 저장된 상품 이미지 파일 이름
//        oriImgName : 업로드했던 상품 이미지 파일의 원래 이름
//        imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
//        이미지를 수정한 경우 상품 이미지 업데이트 
        if(!itemImgFile.isEmpty()){
//            상품 이미지 아이디를 이용하여 기존에 저장했던 상품 이미지 엔티티 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
//            기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+
                        savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
//            업데이트 한 상품 이미지 파일 업로드
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
//            변경된 상품 이미지 정보 세팅. savedItemImg Entity는 데이터를 변경하는 것만으로 변경 감지 기능이 동작하여 트랙젝션이 끝날 때 update 쿼리 실행
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);

        }
    }
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemImgRepository.getAdminItemPage(itemSearchDto, pageable);
    }



}
