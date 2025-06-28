package nova.hackathon.board.market;

import lombok.RequiredArgsConstructor;
import nova.hackathon.board.dto.BoardDTO;
import nova.hackathon.util.ai.gemini.GeminiDTO;
import nova.hackathon.util.response.ApiResponse;
import nova.hackathon.util.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/market")
public class MarketController {

    private final MarketService marketService;

    /**
     * 블로그 글 아이디어 생성 (제목 + 요약 리스트)
     */
    @PostMapping("/plans")
    public ResponseEntity<GeminiDTO.BlogPlanResponse> generateBlogPlans(
            @RequestBody GeminiDTO.ClientNaverBlogPlanRequestDTO request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        GeminiDTO.BlogPlanResponse response = marketService.generateBlogPlanList(request, userPrincipal);
        return ResponseEntity.ok(response);
    }

    // 블로그 글 계획 조회하기
    @GetMapping("/plans")
    public List<BoardDTO> getAllMyFullPlans(
            @AuthenticationPrincipal UserPrincipal principal) {
        return marketService.getAllBoardsForUser(principal);
    }

    @PostMapping()
    public GeminiDTO.GeminiResponse writeNaverBlog(
            @RequestBody GeminiDTO.ClientNaverBlogRequestDTO request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return marketService.writeNaverBlog(request, userPrincipal);
    }

    @PostMapping("/plans/save")
    public ResponseEntity<?> saveBlogPlan(
            @RequestBody GeminiDTO.SaveRequestDTO req,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        marketService.saveBlogPlanList(req, userPrincipal);
        return ResponseEntity.ok().body(ApiResponse.success("성공적으로 저장 되었습니다."));
    }

}

