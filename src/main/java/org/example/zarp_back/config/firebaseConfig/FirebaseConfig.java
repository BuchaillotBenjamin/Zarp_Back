package org.example.zarp_back.config.firebaseConfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FirebaseConfig {

    @Autowired
    private FirebaseProperties firebaseProperties;

    @PostConstruct
    public void init() throws IOException {

        String privateKey = firebaseProperties.getPrivateKey()
                .replace("\\n", "\n"); // 🔥 OBLIGATORIO

        String json = "{"
                + "\"type\": \"" + firebaseProperties.getType() + "\","
                + "\"project_id\": \"" + firebaseProperties.getProjectId() + "\","
                + "\"private_key_id\": \"" + firebaseProperties.getPrivateKeyId() + "\","
                + "\"private_key\": \"" + privateKey + "\","
                + "\"client_email\": \"" + firebaseProperties.getClientEmail() + "\","
                + "\"client_id\": \"" + firebaseProperties.getClientId() + "\","
                + "\"auth_uri\": \"" + firebaseProperties.getAuthUri() + "\","
                + "\"token_uri\": \"" + firebaseProperties.getTokenUri() + "\","
                + "\"auth_provider_x509_cert_url\": \"" + firebaseProperties.getAuthProviderCertUrl() + "\","
                + "\"client_x509_cert_url\": \"" + firebaseProperties.getClientCertUrl() + "\""
                + "}";

        InputStream serviceAccount =
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            log.info("Firebase initialized correctly");
        }
    }
}
