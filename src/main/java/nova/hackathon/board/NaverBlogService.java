package nova.hackathon.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.hackathon.member.Member;
import nova.hackathon.member.MemberRepository;
import nova.hackathon.util.ai.gemini.GeminiDTO;
import nova.hackathon.util.ai.gemini.GeminiService;
import nova.hackathon.util.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverBlogService {
    private final GeminiService geminiService;
    private final MemberRepository memberRepository;

    public GeminiDTO.GeminiResponse writeNaverBlog(GeminiDTO.ClientNaverBlogRequestDTO req, UserPrincipal userPrincipal) {

        Member member = memberRepository.findByEmail(userPrincipal.getEmail()).orElseThrow();
        String storeName = member.getStoreName();
        String name = member.getName();
        Member.ContentsTone contentsTone = member.getContentsTone();

        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("# 주제: 네이버 블로그의 인기 농가 블로그를 작성하고 싶은데, 인기 블로그들의 형식에 따라서 글을 a4 2장 분량으로 작성하고 싶어.\n")
                .append("# 역할: 의성 농부\n")
                .append("# 이미지: 문맥에 맞는 이미지에 대")
                .append("# 출력 형식: HTML 형식으로 블로그 글 출력\n");

        if (name != null) {
            promptBuilder.append("# 농부의 이름: ").append(name).append("\n");
        }

        if (storeName != null) {
            promptBuilder.append("# 판매 가게 명: ").append(storeName).append("\n");
        }

        // 톤 추가
        if (contentsTone != null) {
            promptBuilder.append("# 글 작성 톤: ").append(contentsTone.getDescription()).append("\n");
        }

        if (member.getSmartStoreLink() != null) {
            promptBuilder.append("# 스마트스토어 링크: ").append(member.getSmartStoreLink()).append("\n");
        }

        if (req.contentsType() != null) {
            promptBuilder.append("# 글의 목적: ").append(req.contentsType()).append("\n");
        }
        if (req.contentsType() != null) {
            promptBuilder.append("# 판매 농작물: ").append(req.item()).append("\n");
        }
        if (req.extraPrompt() != null) {
            promptBuilder.append("# 농부가 원하는 컨셉: ").append(req.extraPrompt()).append("\n");
        }

        // Gemini API 호출
        String finalPrompt = promptBuilder.toString();
        log.info("최종 Gemini 프롬프트:\n{}", finalPrompt);

        return geminiService.callGemini(finalPrompt);
    }



}
