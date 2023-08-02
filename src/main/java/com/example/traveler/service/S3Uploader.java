package com.example.traveler.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.traveler.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.example.traveler.config.BaseResponseStatus.PATCH_FAIL_UPLOAD_S3;
import static com.example.traveler.config.BaseResponseStatus.PATCH_NULL_FILE;


@Service
public class S3Uploader {


    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;


    public String uploadImage(MultipartFile imageFile) throws BaseException {
        try {
            if (imageFile.isEmpty()) {
                throw new BaseException(PATCH_NULL_FILE);
            }

            // S3에 업로드할 파일명 생성 (유니크한 이름으로 저장하거나, 원본 파일명을 그대로 사용할 수도 있습니다.)
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

            // S3 버킷에 이미지 업로드
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, imageFile.getInputStream(), null);
            s3Client.putObject(request);

            // 업로드된 이미지의 URL 경로 반환 (여기서는 S3 버킷 URL과 파일명을 조합하여 경로를 만듭니다.)
            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(PATCH_FAIL_UPLOAD_S3);
        }
    }

}
