package nova.hackathon.util.ai.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.hackathon.util.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    private final WebClient geminiClient;


    public GeminiService(@Qualifier("geminiClient") WebClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public GeminiDTO.GeminiResponse callGemini(String prompt) {
        var part = new GeminiDTO.GeminiRequest.Content.Part(prompt);
        var content = new GeminiDTO.GeminiRequest.Content(List.of(part));
        var request = new GeminiDTO.GeminiRequest(List.of(content));

        return geminiClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/models/gemini-2.5-pro:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiDTO.GeminiResponse.class)
                .block();
    }

    public String generateImageFromPrompt(String prompt) {
        var part = new GeminiDTO.GeminiRequest.Content.Part(prompt);
        var content = new GeminiDTO.GeminiRequest.Content(List.of(part));
        var requestBody = new ImageRequest
                (
                List.of(content),
                new GenerationConfig(List.of("TEXT", "IMAGE"))
        );

        return geminiClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.0-flash-preview-image-generation:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class) // 응답 전체를 받아 직접 파싱
                .map(this::extractBase64ImageData)
                .block();
    }
    public record ImageRequest(
            List<GeminiDTO.GeminiRequest.Content> contents,
            @JsonProperty("generation_config")
            GenerationConfig generationConfig
    ) {}

    public record GenerationConfig(
            @JsonProperty("response_modalities")
            List<String> responseModalities
    ) {}

    private String extractBase64ImageData(String responseJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode parts = mapper.readTree(responseJson)
                    .at("/candidates/0/content/parts");
            for (JsonNode part : parts) {
                if (part.has("inline_data")            // ← snake_case
                        && part.path("inline_data").has("data")) {
                    return part.path("inline_data").path("data").asText();
                }
            }
            throw new IllegalStateException("이미지 파트를 찾지 못했습니다.");
        } catch (Exception e) {
            // 응답 전문을 로그에 남겨 디버깅
            log.error("Gemini 응답 파싱 실패: {}", responseJson);
            throw new RuntimeException(e);
        }
    }
}
