package nova.hackathon.util.ai.gemini.image.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagenService {

    private final WebClient geminiClient;
    private final Storage storage;

    @Value("${gemini.api-key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BUCKET_NAME = "uhackathon";

    public String generateAndUploadImage(String prompt, Long boardId) {
        try {
            // 1. 이미지 생성 요청
            String requestJson = objectMapper.writeValueAsString(Map.of(
                    "instances", java.util.List.of(Map.of("prompt", prompt)),
                    "parameters", Map.of("sampleCount", 1)
            ));

            String responseJson = geminiClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/imagen-3.0-generate-002:predict")
                            .queryParam("key", apiKey)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // 2. base64 추출
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode prediction = root.at("/predictions/0");
            String mimeType = prediction.path("mimeType").asText("image/png");
            String base64 = prediction.path("bytesBase64Encoded").asText();

            if (base64 == null || base64.isBlank()) {
                throw new IllegalStateException("base64 이미지 데이터가 없습니다.");
            }

            // 3. byte[] 변환
            byte[] imageBytes = Base64.getDecoder().decode(base64);

            // 4. 파일 이름 생성
            String extension = mimeType.equals("image/jpeg") ? ".jpg" : ".png";
            String fileName = UUID.randomUUID() + extension;
            String objectPath = String.format("user/%d/%s", boardId, fileName);

            // 5. GCS 업로드
            BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, objectPath)
                    .setContentType(mimeType)
                    .build();
            storage.create(blobInfo, imageBytes);

            // 6. Public URL 반환
            return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + objectPath;

        } catch (Exception e) {
            log.error("이미지 생성 및 업로드 실패", e);
            throw new RuntimeException("이미지 생성 및 업로드 실패", e);
        }
    }
}
