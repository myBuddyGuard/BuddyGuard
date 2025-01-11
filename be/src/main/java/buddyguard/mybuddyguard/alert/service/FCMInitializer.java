package buddyguard.mybuddyguard.alert.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FCMInitializer {

    @Value("${fcm.type}")
    private String type;

    @Value("${fcm.project_id}")
    private String projectId;

    @Value("${fcm.private_key_id}")
    private String privateKeyId;

    @Value("${fcm.private_key}")
    private String privateKey;

    @Value("${fcm.client_email}")
    private String clientEmail;

    @Value("${fcm.client_id}")
    private String clientId;

    @Value("${fcm.auth_uri}")
    private String authUri;

    @Value("${fcm.token_uri}")
    private String tokenUri;

    @Value("${fcm.auth_provider_x509_cert_url}")
    private String authProviderX509CertUrl;

    @Value("${fcm.client_x509_cert_url}")
    private String clientX509CertUrl;

    @PostConstruct
    public void initialize() throws IOException {
        // 개행 문자를 복구한 private_key 처리
        String formattedPrivateKey = privateKey.replace("\\n", "\n");

        // FCM 설정을 Map으로 구성
        Map<String, Object> credentialsMap = new HashMap<>();
        credentialsMap.put("type", type);
        credentialsMap.put("project_id", projectId);
        credentialsMap.put("private_key_id", privateKeyId);
        credentialsMap.put("private_key", formattedPrivateKey);
        credentialsMap.put("client_email", clientEmail);
        credentialsMap.put("client_id", clientId);
        credentialsMap.put("auth_uri", authUri);
        credentialsMap.put("token_uri", tokenUri);
        credentialsMap.put("auth_provider_x509_cert_url", authProviderX509CertUrl);
        credentialsMap.put("client_x509_cert_url", clientX509CertUrl);

        // Map을 JSON 문자열로 변환 후 Firebase에 전달
        String credentialsJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(credentialsMap);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
