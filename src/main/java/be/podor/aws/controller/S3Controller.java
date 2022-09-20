package be.podor.aws.controller;

import be.podor.aws.dto.ImageUrlDto;
import be.podor.aws.dto.S3Dto;
import be.podor.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // 이미지 업로드
    @PostMapping("/api/image/upload")
    public ResponseEntity<?> uploadImage(@RequestPart("image") List<MultipartFile> multipartFiles) throws IOException {
        S3Dto responseDto = s3Service.s3FileUpload(multipartFiles);

        return ResponseEntity.ok(responseDto);
    }

    // 이미지 삭제
    @DeleteMapping("/api/image/delete")
    public ResponseEntity<?> deleteImage(@RequestBody ImageUrlDto requestBody) {
        s3Service.s3FileDelete(requestBody);

        return ResponseEntity.ok().build();
    }
}
