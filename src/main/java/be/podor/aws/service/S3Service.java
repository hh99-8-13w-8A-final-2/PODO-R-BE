package be.podor.aws.service;

import be.podor.aws.dto.ImageUrlDto;
import be.podor.aws.dto.S3Dto;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final int IMAGE_WIDTH = 800;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 업로드
    public S3Dto s3FileUpload(List<MultipartFile> multipartFiles) throws IOException {

        List<String> imgurls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {

            String fileName = UUID.randomUUID().toString();

            BufferedImage image = resizeImage(multipartFile, IMAGE_WIDTH);
            String contentType = multipartFile.getContentType();
            ObjectMetadata objectMetadata = new ObjectMetadata();

            ByteArrayInputStream inputStream = imageToInputStream(image, contentType, objectMetadata);

            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket,
                    fileName,
                    inputStream,
                    objectMetadata
            );

            amazonS3.putObject(putObjectRequest);

            imgurls.add(amazonS3.getUrl(bucket, fileName).toString());
        }

        return new S3Dto(imgurls);
    }

    public void s3FileDelete(ImageUrlDto requestBody) {
        String[] split = requestBody.getImageUrl().split("/");
        String key = split[split.length - 1].trim();

        amazonS3.deleteObject(bucket, key);
    }

    // image resize
    private BufferedImage resizeImage(MultipartFile multipartFile, int targetWidth) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

        if (bufferedImage.getWidth() < targetWidth) {
            return bufferedImage;
        }

        return Scalr.resize(bufferedImage, targetWidth);
    }

    // image to InputStream
    private ByteArrayInputStream imageToInputStream(BufferedImage bufferedImage, String contentType, ObjectMetadata objectMetadata) throws IOException {
        String fileExtension = contentType.split("/")[1];

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, fileExtension, byteArrayOutputStream);

        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(byteArrayOutputStream.size());

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}