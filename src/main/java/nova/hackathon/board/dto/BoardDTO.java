package nova.hackathon.board.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardDTO {
    private String contentsType;
    private String title;
    private String summary;
    private String conceptTitle;
    private String status;
    private int ver;
}
