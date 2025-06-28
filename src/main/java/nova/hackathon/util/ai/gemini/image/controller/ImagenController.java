package nova.hackathon.util.ai.gemini.image.controller;

import lombok.RequiredArgsConstructor;
import nova.hackathon.util.ai.gemini.image.service.ImagenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService imagenGCSService;

    /**
     * 프롬프트 기반 이미지 생성 + GCS 업로드
     */
    @PostMapping("/generate")
    public String generateAndUpload(
            @RequestParam String prompt,
            @RequestParam Long boardId
    ) {
        return imagenGCSService.generateAndUploadImage(prompt, boardId);
    }
}
