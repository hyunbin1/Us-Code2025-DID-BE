package nova.hackathon.util.ai.gemini;

import java.util.List;

public class GeminiDTO {
    public record ClientNaverBlogRequestDTO(
            String extraPrompt,
            String contentsType,
            String item
    ) {

    }

    // 클라이언트 요청용 DTO
    public record ClientRequestDTO(String question) {}




    // Gemini API 요청 포맷
    public record GeminiRequest(List<Content> contents) {
        public record Content(List<Part> parts) {
            public record Part(String text) {}
        }
    }

    // Gemini API 응답 포맷
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
}
