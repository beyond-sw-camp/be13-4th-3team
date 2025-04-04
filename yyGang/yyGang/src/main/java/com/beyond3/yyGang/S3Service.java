package com.beyond3.yyGang;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.bucket}")
    private String bucket;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // 업로드 할 image 를 받아서 처리 ->
    public String uploadImage(MultipartFile image) throws IOException {
        // UUID -> 고유 ID + 원본 사진 => 고유 사진 파일 이름 생성
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        // 파일의 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());    // 업로드 되는 파일의 MIME 타입 지정 ex) image.jpg, image.png
        metadata.setContentLength(image.getSize());     // 업로드 파일의 크기를 바이트 단위로 설정

        // S3에 파일 업로드 요청 생성
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);

        // S3에 파일 업로드
        amazonS3.putObject(putObjectRequest);

        // 파일이 성공적으로 업로드된 경우 -> https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/<파일명> 의 이름으로 URL 생성
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
    }


}
