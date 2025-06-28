package nova.hackathon.util.ai.gemini.image;


import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiImageService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/imagegeneration@001")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    private static final String SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private static final String KEY_PATH = "gcp-key.json"; // resources 하위 기준

    public String generateImageFromPrompt(String prompt) {
        try {
            String token = getAccessTokenFromKey();

            Map<String, Object> body = Map.of(
                    "prompt", Map.of("text", prompt),
                    "imageDetail", "HIGH",
                    "languageCode", "ko"
            );

            String response = webClient.post()
                    .uri("/generateImage")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Gemini 이미지 응답: {}", response);
            return response;

        } catch (Exception e) {
            log.error("Gemini 이미지 생성 실패", e);
            throw new RuntimeException("Gemini 이미지 생성 중 오류", e);
        }
    }

    private String getAccessTokenFromKey() throws Exception {
        ClassPathResource resource = new ClassPathResource(KEY_PATH);
        try (InputStream input = resource.getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(input)
                    .createScoped(List.of(SCOPE));
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        }
    }
}
