package buddyguard.mybuddyguard.exception;

import org.springframework.http.HttpStatus;

public class S3ImageUploadFailException extends BusinessException {

    public S3ImageUploadFailException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일 업로드가 실패하였습니다.");
    }
}
