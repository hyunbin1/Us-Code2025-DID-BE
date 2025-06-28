package nova.hackathon.util.ai.gemini.image.dto;

import java.util.List;

public class ImagenDTO {

    /* 요청 DTO */
    public record ImagenRequest(
            List<Instance> instances,
            Parameters parameters
    ) {
        public record Instance(String prompt) {}
        public record Parameters(int sampleCount) {}
    }

    /* 응답 DTO */
    public record ImagenResponse(List<Prediction> predictions) {
        public record Prediction(String mimeType, String bytesBase64Encoded) {}
    }

    /* 컨트롤러용 입력 DTO */
    public record GenerateRequest(String prompt, int sampleCount) {}
}

