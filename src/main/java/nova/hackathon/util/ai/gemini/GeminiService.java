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

}
