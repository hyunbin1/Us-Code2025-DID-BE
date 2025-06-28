package nova.hackathon.util.ai.gemini;

import lombok.RequiredArgsConstructor;
import nova.hackathon.util.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping
    public ApiResponse<GeminiDTO.GeminiResponse> askGemini(@RequestBody GeminiDTO.ClientRequestDTO request) {
        GeminiDTO.GeminiResponse response = geminiService.callGemini(request.question());
        return ApiResponse.success(response);
    }
    @PostMapping("/image")
    public ApiResponse<String> generateImage(@RequestBody GeminiDTO.ClientRequestDTO request) {
        String base64Image = geminiService.generateImageFromPrompt(request.question());
        return ApiResponse.success(base64Image); // base64 인코딩된 이미지 데이터
    }

}
