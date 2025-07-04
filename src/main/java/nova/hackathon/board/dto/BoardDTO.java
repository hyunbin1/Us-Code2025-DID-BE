package nova.hackathon.board.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardDTO {
    private String contentsType;
    private String title;
    private String summary;
    private String contentsTitle;
    private String status;
    private String item;
    private int ver;
}
