package be.podor.aws.service;

import be.podor.aws.dto.S3Dto;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 업로드
    public S3Dto s3FileUpload(MultipartFile multipartFile) throws IOException {

        String fileName = UUID.randomUUID().toString();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucket,
                fileName,
                multipartFile.getInputStream(),
                objectMetadata
        );

        amazonS3.putObject(putObjectRequest);

        String url = amazonS3.getUrl(bucket, fileName).toString();

        return new S3Dto(url);
    }
}
