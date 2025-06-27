package nova.hackathon.util.mail;

import lombok.Builder;
import lombok.Data;

public class EmailDTO {

    @Data
    @Builder
    public static class Request {
        private String to;          // 수신자
        private String subject;     // 제목
        private String content;     // 본문
        private boolean html;       // true = HTML, false = 평문
    }

    @Data
    @Builder
    public static class Response {
        private boolean success;    // 전송 성공 여부
        private String messageId;   // Gmail Message-ID 등
        private String error;       // 실패 시 에러 메시지
    }
}
