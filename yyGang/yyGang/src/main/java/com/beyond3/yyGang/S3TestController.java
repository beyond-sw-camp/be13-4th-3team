package com.beyond3.yyGang;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3TestController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = s3Service.uploadImage(file);
            return ResponseEntity.ok( imageUrl);
        } catch (Exception e) {
            return ResponseEntity.ok( "파일 업로드에 실패했습니다.");
        }
    }

}
