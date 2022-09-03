package be.podor.aws.controller;

import be.podor.aws.dto.S3Dto;
import be.podor.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // 이미지 업로드
    // Todo member permit
    @PostMapping("/api/image/upload")
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile multipartFile) throws IOException {
        S3Dto responseDto = s3Service.s3FileUpload(multipartFile);

        return ResponseEntity.ok(responseDto);
    }
}
