package com.beyond3.yyGang;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// S3 Client 설정 정보를 빈으로 등록함
@Configuration
public class S3Config {

    // value : 스프링의 프로퍼티 주입 -> application properties에서 정의된 값을 주입받음
    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;   // 공개키 값

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;   // 비밀키 값

    @Value("${spring.cloud.aws.region.static}")
    private String region;  // 지역 값

    @Bean   // AmazonS3 객체 생성 -> Spring 내 S3와의 통신 담당
    public AmazonS3 amazonS3() {
        // 자격 증명을 위한 비밀키, 개인키
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard() // 기본 AmazonS3 클라이언트 설정
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
