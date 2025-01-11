package buddyguard.mybuddyguard.walkimage.service.impl;

import buddyguard.mybuddyguard.walkimage.entity.WalkS3Image;
import buddyguard.mybuddyguard.walkimage.repository.WalkS3ImageRepository;
import buddyguard.mybuddyguard.walkimage.service.WalkImageService;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WalkImageServiceImpl implements WalkImageService {

    private final AmazonS3Client amazonS3Client;
    private final WalkS3ImageRepository walkS3ImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String WALK_IMG_DIR = "walk";

    @Override
    public WalkS3Image uploadWalkImage(MultipartFile multipartFile) {
        try {
            // 1. 파일 이름 생성
            String fileName = WALK_IMG_DIR + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

            // 2. S3에 업로드하기 위한 메타데이터 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            // 3. S3에 파일 업로드 (InputStream을 바로 S3로 전송)
            try (InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
            }

            // 4. S3에 저장된 파일의 URL 가져오기
            String imageUrl = amazonS3Client.getUrl(bucket, fileName).toString();

            // 5. WalkS3Image 엔티티 생성 및 저장
            WalkS3Image walkS3Image = WalkS3Image.builder()
                    .imageUrl(imageUrl)
                    .fileName(fileName)
                    .fileType(multipartFile.getContentType())
                    .fileSize(multipartFile.getSize())
                    .build();

            return walkS3ImageRepository.save(walkS3Image);

        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
