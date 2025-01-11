package buddyguard.mybuddyguard.exception;

import org.springframework.http.HttpStatus;


public class ImageFileRequiredException extends BusinessException{

    public ImageFileRequiredException() {
        super(HttpStatus.BAD_REQUEST, "이미지 파일이 필요합니다.");
    }

}
