package nova.hackathon.util.ElasticSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {
    private String id;
    private String highlightedTitle;
    private String highlightedContent;
    private LocalDateTime date;
    private String link;
    private String category;
    private String type;
}