package nova.hackathon.util.ai.gemini.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gemini-image")
public class GeminiImageController {

    private final GeminiImageService geminiImageService;

    @GetMapping
    public ResponseEntity<String> generateImage(@RequestParam String prompt) {
        String result = geminiImageService.generateImageFromPrompt(prompt);
        return ResponseEntity.ok(result);
    }
}
