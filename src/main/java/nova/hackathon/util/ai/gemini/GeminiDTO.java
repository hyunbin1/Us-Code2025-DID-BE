package nova.hackathon.util.ai.gemini;

import nova.hackathon.member.Member;

import java.util.List;

public class GeminiDTO {

    public record ClientNaverBlogPlanRequestDTO(
            String contentsType, // 블로그 주제
            String item, // 농산물 선택
            List<String> keywords,
            int count,
            Member.Platform type
    ) {}

    public record BlogPlan(
            String title,
            String summary
    ) {}

    public record ClientNaverBlogRequestDTO(
            String contentsType, // 블로그 주제
            String item, // 농산물 선택
            String blogTitlePrompt,
            String blogContentsPrompt
    ) {}

    public record ClientRequestDTO(String question) {}

    public record GeminiRequest(List<Content> contents) {
        public record Content(List<Part> parts) {
            public record Part(String text) {}
        }
    }

    public record GeminiResponse(
            List<Candidate> candidates,
            UsageMetadata usageMetadata,
            String modelVersion,
            String responseId
    ) {
        public record Candidate(Content content, String finishReason, Double avgLogprobs) {
            public record Content(List<Part> parts, String role) {
                public record Part(String text) {}
            }
        }

        public record UsageMetadata(
                int promptTokenCount,
                int candidatesTokenCount,
                int totalTokenCount,
                List<TokenDetail> promptTokensDetails,
                List<TokenDetail> candidatesTokensDetails
        ) {
            public record TokenDetail(String modality, int tokenCount) {}
        }
    }

    public record SaveRequestDTO(
            String conceptTitle,
            String item,
            String contentsType,
            List<String> keywords,
            Member.Platform type,
            List<GeminiDTO.BlogPlan> plans
    ) {}

    public record BlogPlanResponse(
            String contentTitle,
            List<GeminiDTO.BlogPlan> plans
    ) {}

}
