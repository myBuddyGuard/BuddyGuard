package buddyguard.mybuddyguard.invitation.utils;

import jakarta.annotation.PostConstruct;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvitationLinkGenerator {

    @Value("${app.base-url}")
    private String baseUrlInstance;
    private static String baseUrl;

    /**
     * bean 생성이 끝난 후, 해당 메서드를 실행하여 yaml 파일 값을 세팅해 줍니다.
     */
    @PostConstruct
    public void init() {
        baseUrl = baseUrlInstance;
    }

    /**
     * 버전 4 UUID 값을 생성합니다.
     *
     * @return 생성한 UUID 객체
     */
    public static UUID generateUuid() {
        return UUID.randomUUID();
    }

    /**
     * UUID 값을 받아 초대링크를 생성합니다.
     *
     * @param uuid
     * @return 초대 링크 String
     */
    public static String generateLink(String uuid) {
        return baseUrl + "/invitation/" + uuid;
    }
}
