package buddyguard.mybuddyguard.walkimage.service;

import buddyguard.mybuddyguard.walkimage.entity.WalkS3Image;
import org.springframework.web.multipart.MultipartFile;

public interface WalkImageService {
    WalkS3Image uploadWalkImage(MultipartFile multipartFile);
}


