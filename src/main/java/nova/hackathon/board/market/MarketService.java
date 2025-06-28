package nova.hackathon.board.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.hackathon.board.Board;
import nova.hackathon.board.BoardRepository;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class MarketService {
    private final GeminiService geminiService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    /**
     * 블로그 계획(Plan)을 Gemini로부터 생성하고, Board 엔티티에 저장한다.
     */
    @Transactional
    public GeminiDTO.BlogPlanResponse generateBlogPlanList(
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
        promptBuilder.append("# 역할: 네이버 스마트 스토어에 정통한 농부\n");
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
                        + "위 정보에 따라 네이버 블로그용 아이디어를 ").append(req.count()).append("개 제안해주세요.\n" + "각 아이디어는 아래 형식으로 구성해 주세요(contentsTitle은 맨 처음에 무조건 한번만 나옵니다.):\n" +
                "contentsTitle: ...\n" +

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
        log.info("[Gemini 응답 원문]\n{}", rawResponse);

        // contentsTitle 추출
        String contentsTitle = "Untitled";
        Matcher conceptMatcher = Pattern.compile("contentsTitle\\s*:\\s*(.+)", Pattern.CASE_INSENSITIVE)
                .matcher(rawResponse);
        if (conceptMatcher.find()) {
            contentsTitle = conceptMatcher.group(1).trim();
        }

        // 제목/요약 파싱만 수행 (저장 X)
        Pattern ideaPtn = Pattern.compile(
                "제목\\s*:\\s*(.*?)\\s*요약\\s*:\\s*(.*?)(?=(\\n제목|$))",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher ideaMatcher = ideaPtn.matcher(rawResponse);

        List<GeminiDTO.BlogPlan> plans = new ArrayList<>();
        int cnt = 0;
        while (ideaMatcher.find() && cnt < req.count()) {
            String title = ideaMatcher.group(1).trim();
            String summary = ideaMatcher.group(2).trim();
            plans.add(new GeminiDTO.BlogPlan(title, summary));
            cnt++;
        }

        return new GeminiDTO.BlogPlanResponse(contentsTitle, plans); // 저장은 하지 않음
    }

    @Transactional
    public void saveBlogPlanList(GeminiDTO.SaveRequestDTO req, UserPrincipal userPrincipal) {
        Member member = memberRepository.findByEmail(userPrincipal.getEmail()).orElseThrow();

        int latestVer = boardRepository.findMaxVersionByMemberAndType(member, req.type()).orElse(0);
        int newVer = latestVer + 1;
        if (newVer >= 4) {
            throw new IllegalStateException("버전이 4 이상이므로 더 이상 저장할 수 없습니다.");
        }

        List<Board> boards = req.plans().stream()
                .map(plan -> Board.builder()
                        .member(member)
                        .title(plan.title())
                        .summary(plan.summary())
                        .content(null)
                        .status(Board.Status.PENDING)
                        .type(req.type())
                        .ver(newVer)
                        .contentsTitle(req.contentsTitle())
                        .item(req.item())
                        .contentsType(req.contentsType())
                        .keywords(req.keywords())
                        .build())
                .collect(Collectors.toList());


        boardRepository.saveAll(boards);
    }

    public String writeMarket(GeminiDTO.ClientNaverBlogRequestDTO req, UserPrincipal userPrincipal) {

        Member member = memberRepository.findByEmail(userPrincipal.getEmail()).orElseThrow();
        String storeName = member.getStoreName();
        String name = member.getName();
        Member.ContentsTone contentsTone = member.getContentsTone();
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("# 주제: 네이버 스마트스토어의 판매량 1위하는 매우 유명한 농가 상품 상세페이지를 작성하고 싶은데, 인기 스토어들의 " +
                        "레이아웃과 문체를 참고해서 A4 2장 분량으로 만들고 싶어.\n")
                .append("# 역할: 직접 수확하고 재배한 것을 판매하는 스마트스토어 판매자\n")
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
        String resultText = firstDraft.candidates().stream()
                .findFirst()                               // 첫 번째 후보
                .flatMap(c -> c.content().parts().stream() // 후보의 첫 번째 part
                        .findFirst()
                        .map(GeminiDTO.GeminiResponse
                                .Candidate.Content.Part::text))
                .orElse("");                               // 못 찾을 경우 빈 문자열

        return resultText;
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
                        .contentsTitle(board.getContentsTitle())
                        .status(board.getStatus().name())
                        .ver(board.getVer())
                        .build())
                .toList();
    }


}
