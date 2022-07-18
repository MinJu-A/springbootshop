package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
//        UUID : 서로 다른 개체들을 구별하기 위해 이름을 부여할 때 사용 파일명 중복 문제 해결 가능
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        UUID로 받은 값과 파일의 이름의 확장자를 조합해서 저장할 파일 이름을 만든다. 
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
//        파일에 쓸 파일 출력 스트림을 생성
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
//        fileData를 파일 출력 스트림에 입력한다. 
        fos.write(fileData);
        fos.close();
//        업로드 된 파일 이름을 리턴
        return savedFileName;
    }


}
