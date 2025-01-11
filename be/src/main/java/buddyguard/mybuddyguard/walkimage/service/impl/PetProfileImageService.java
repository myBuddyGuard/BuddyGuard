package buddyguard.mybuddyguard.walkimage.service.impl;

import buddyguard.mybuddyguard.exception.S3ImageUploadFailException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PetProfileImageService {

    private final AmazonS3Client amazonS3Client;
    private static final String PET_PROFILE_IMG_DIR = "petProfile";
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PetProfileImageService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadPetProfileImage(MultipartFile multipartFile) {
        String fileName = PET_PROFILE_IMG_DIR + "/" + UUID.randomUUID() + "-"
                + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new S3ImageUploadFailException();
        }
    }
}
