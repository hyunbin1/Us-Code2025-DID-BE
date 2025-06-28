package nova.hackathon.board;

import jakarta.persistence.*;
import lombok.*;
import nova.hackathon.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String summary;

    @Lob
    private String content; // 본문은 나중에 추가됨

    private int ver;
    private String contentsTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Member.Platform type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String item;  // 마늘, 흑마늘 등

    @Column(nullable = false)
    private String contentsType; // 상품홍보, 일상공유 등

    @ElementCollection
    @CollectionTable(name = "board_keywords", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();


    public enum Status {
        PENDING,     // 요약까지 생성된 상태
        GENERATED    // 본문까지 생성된 상태
    }

    public void updateContent(String content) {
        this.content = content;
        this.status = Status.GENERATED;
    }

}
