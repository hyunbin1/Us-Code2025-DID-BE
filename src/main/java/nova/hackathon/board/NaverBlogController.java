package nova.hackathon.board;

import lombok.RequiredArgsConstructor;
import nova.hackathon.board.dto.BoardDTO;
import nova.hackathon.util.ai.gemini.GeminiDTO;
import nova.hackathon.util.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/naver-blog")
public class NaverBlogController {

    private final NaverBlogService naverBlogService;


    /**
     * 블로그 글 아이디어 생성 (제목 + 요약 리스트)
     */
    @PostMapping("/plans")
    public List<GeminiDTO.BlogPlan> generateBlogPlans(
            @RequestBody GeminiDTO.ClientNaverBlogPlanRequestDTO request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return naverBlogService.generateBlogPlanList(request, userPrincipal);
    }

    // 블로그 글 계획 조회하기
    @GetMapping("/plans")
    public List<BoardDTO> getAllMyFullPlans(
            @AuthenticationPrincipal UserPrincipal principal) {
        return naverBlogService.getAllBoardsForUser(principal);
    }

    @PostMapping()
    public GeminiDTO.GeminiResponse writeNaverBlog(
            @RequestBody GeminiDTO.ClientNaverBlogRequestDTO request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return naverBlogService.writeNaverBlog(request, userPrincipal);
    }
}
