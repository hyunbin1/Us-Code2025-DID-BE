package nova.hackathon.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.hackathon.board.dto.BoardDTO;
import nova.hackathon.member.Member;
import nova.hackathon.member.MemberRepository;
import nova.hackathon.util.ai.gemini.GeminiDTO;
import nova.hackathon.util.ai.gemini.GeminiService;
import nova.hackathon.util.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class NaverBlogService {
    private final GeminiService geminiService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    /**
     * 블로그 계획(Plan)을 Gemini로부터 생성하고, Board 엔티티에 저장한다.
     */
    @Transactional
    public List<GeminiDTO.BlogPlan> generateBlogPlanList(
            GeminiDTO.ClientNaverBlogPlanRequestDTO req, UserPrincipal userPrincipal) {

        Member member = memberRepository.findByEmail(userPrincipal.getEmail()).orElseThrow();

        // 최신 ver 조회 → 새 ver 결정
        int latestVer = boardRepository.findMaxVersionByMemberAndType(member, req.type()).orElse(0);
        int newVer = latestVer + 1;

        String storeName = member.getStoreName();
        String name = member.getName();
        Member.ContentsTone contentsTone = member.getContentsTone();

        // 공통 프롬프트 설정
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("# 역할: 농부\n");
        promptBuilder.append("# 조건: ** ** 혹은 \\n 등 특수문자는 사용하지 않는다.");
        if (name != null) promptBuilder.append("# 농부의 이름: ").append(name).append("\n");
        if (storeName != null) promptBuilder.append("# 판매 가게 명: ").append(storeName).append("\n");
        if (contentsTone != null)
            promptBuilder.append("# 글의 특성 및 글의 성격: ").append(contentsTone.getDescription()).append("\n");

        if (req.item() != null) promptBuilder.append("# 판매 농작물: ").append(req.item()).append("\n");
        if (req.contentsType() != null) promptBuilder.append("# 글의 목적: ").append(req.contentsType()).append("\n");
        if (req.contentsType() != null) promptBuilder.append("# 글의 핵심 요구사항: ").append(req.keywords()).append("\n");

        // 프롬프트 명시
        promptBuilder.append(
                "\n---\n" +
                        "해당 서비스는 네이버 인기 농가 블로그의 한달 게시 목록을 뽑아주는 스케줄러입니다."
                        + "위 정보에 따라 네이버 블로그용 아이디어를 ").append(req.count()).append("개 제안해주세요.\n" +
                "각 아이디어는 아래 형식으로 구성해 주세요:\n" +
                "conceptTitle: ...\n" +

                "제목: ...\n" +
                "요약: ...\n\n" +
                "제목: ...\n" +
                "요약: ...\n\n"
        );

        String prompt = promptBuilder.toString();
        log.info("Gemini 블로그 아이디어 생성 프롬프트:\n{}", prompt);

        // 2. Gemini 호출 및 응답 텍스트 추출
        String rawResponse = geminiService.callGemini(prompt)
                .candidates().get(0)
                .content().parts().get(0)
                .text();

// ----------------------- 파싱 + 저장 -----------------------
        List<GeminiDTO.BlogPlan> plans = new ArrayList<>();
        List<Board> boards = new ArrayList<>();

// (1) conceptTitle 은 1개만
        String conceptTitle = "Untitled";
        Matcher conceptMatcher = Pattern.compile("conceptTitle\\s*:\\s*(.+)", Pattern.CASE_INSENSITIVE)
                .matcher(rawResponse);
        if (conceptMatcher.find()) {
            conceptTitle = conceptMatcher.group(1).trim();
        }

// (2) 제목/요약 N개 파싱
        Pattern ideaPtn = Pattern.compile(
                "제목\\s*:\\s*(.*?)\\s*요약\\s*:\\s*(.*?)(?=(\\n제목|$))",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher ideaMatcher = ideaPtn.matcher(rawResponse);

        int cnt = 0;
        while (ideaMatcher.find() && cnt < req.count()) {
            String title = ideaMatcher.group(1).trim();
            String summary = ideaMatcher.group(2).trim();

            // DTO 리스트
            plans.add(new GeminiDTO.BlogPlan(title, summary));

            // (3) Board 엔티티 생성
            boards.add(
                    Board.builder()
                            .member(member)
                            .title(title)
                            .summary(summary)
                            .content(null)                     // 본문은 이후 생성
                            .status(Board.Status.PENDING)
                            .type(req.type())
                            .ver(newVer)
                            .conceptTitle(conceptTitle)        // ★ 공통 폴더명
                            .item(req.item())
                            .contentsType(req.contentsType())
                            .keywords(req.keywords() != null ? req.keywords() : List.of())
                            .build()
            );
            cnt++;
        }

// 한 번에 INSERT
        boardRepository.saveAll(boards);
        return plans;
    }


    public GeminiDTO.GeminiResponse writeNaverBlog(GeminiDTO.ClientNaverBlogRequestDTO req, UserPrincipal userPrincipal) {

        Member member = memberRepository.findByEmail(userPrincipal.getEmail()).orElseThrow();
        String storeName = member.getStoreName();
        String name = member.getName();
        Member.ContentsTone contentsTone = member.getContentsTone();

        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("# 주제: 네이버 블로그의 인기 농가 블로그를 작성하고 싶은데, 인기 블로그들의 형식에 따라서 글을 a4 2장 분량으로 작성하고 싶어.\n")
                .append("# 역할: 의성 농부\n")
                .append("줄바꿈 문자나 `<br>`, `<br/>` 같은 HTML 태그는 **출력에 포함하지 마**.");

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
        if (req.blogContentsPrompt() != null) {
            promptBuilder.append("# 농부가 원하는 컨셉: ").append(req.blogContentsPrompt()).append("\n");
        }
        if (req.blogTitlePrompt() != null) {
            promptBuilder.append("# 농부가 원하는 제목: ").append(req.blogTitlePrompt()).append("\n");
        }

        // Gemini API 호출
        String finalPrompt = promptBuilder.toString();
        log.info("최종 Gemini 프롬프트:\n{}", finalPrompt);

        GeminiDTO.GeminiResponse firstDraft = geminiService.callGemini(finalPrompt);  // 원본 생성
        return firstDraft;
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public List<BoardDTO> getAllBoardsForUser(UserPrincipal principal) {

        Member member = memberRepository.findByEmail(principal.getEmail())
                .orElseThrow();

        return boardRepository.findAllByMemberOrderByVerDescIdAsc(member).stream()
                .map(board -> BoardDTO.builder()
                        .contentsType(board.getContentsType())
                        .title(board.getTitle())
                        .summary(board.getSummary())
                        .conceptTitle(board.getConceptTitle())
                        .status(board.getStatus().name())
                        .ver(board.getVer())
                        .build())
                .toList();
    }


}
