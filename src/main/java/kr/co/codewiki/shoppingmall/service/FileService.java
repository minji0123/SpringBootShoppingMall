package kr.co.codewiki.shoppingmall.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
/*
UUID: Universally Unique Identifier
서로 다른 개체들을 구별하기 위해서 이름을 부여할 때 사용
실제로는 중복될 가능성이 거의 없어서 그냥 파일 이름으로 사용하면 됨
 */

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{

        UUID uuid = UUID.randomUUID(); // 완전 랜덤하게 해시나 솔트처럼!
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // UUID 로 받은 값 + 원래 파일 확장자를 조합. (savedFileName: 저장될 파일 이름)
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // (FileOutputStream: 출력값이 바이트)
        // 생성자로 파일이 저장될 위치와, 파일의 이름을 넘겨서, 파일에 쓸 파일 출력 스트림을 만듦
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        fos.write(fileData); // fileData 를 파일 스트림에 입력함
        fos.close();

        return savedFileName; // 파일 이름만 반환
    }

    public void deleteFile(String filePath) throws Exception{

        File deleteFile = new File(filePath); // 파일 저장 경로를 이용해서 파일 객체 생성

        if(deleteFile.exists()) { // 해당 파일이 존재하면은 파일을 삭제한닷!
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
