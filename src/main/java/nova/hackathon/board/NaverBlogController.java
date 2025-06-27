package nova.hackathon.board;

import lombok.RequiredArgsConstructor;
import nova.hackathon.util.ai.gemini.GeminiDTO;
import nova.hackathon.util.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/naver-blog")
public class NaverBlogController {

    private final NaverBlogService naverBlogService;

    @PostMapping()
    public GeminiDTO.GeminiResponse writeNaverBlog(
            @RequestBody GeminiDTO.ClientNaverBlogRequestDTO request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return naverBlogService.writeNaverBlog(request, userPrincipal);
    }
}
