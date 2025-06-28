package nova.hackathon.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private UUID uuid;
    private String name;
    private String email;
    private String contactEmail;
    private String gender;
    private String nickname;
    private Boolean introStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Member.Platform> platform;
    private LocalTime alarmTime;
    private Member.Role role;
    private String address; // 판매자 상세 주소
    private String phoneNumber; // 판매자 휴대번호
    private String kakaoId; // 판매자 카톡아이디
    private String storeName; // 판매자 가게명
    private String snsId; // 판매자 SNS ID
    private String smartStoreLink; // 스마트 스토어 주소
    private Member.ContentsTone contentsTone; // 글 작성 톤
    private Member.AlarmFrequency alarmFrequency; // 글 작성 톤

    /**
     * Member 엔티티를 MemberDTO로 변환하는 메서드 (응답용)
     */
    public static MemberDTO fromEntity(Member member) {
        return MemberDTO.builder()
                .uuid(member.getUuid())
                .name(member.getName())
                .email(member.getEmail())
                .email(member.getContactEmail())
                .gender(String.valueOf(member.getGender ()))
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .role(member.getRole())
                .alarmTime(member.getAlarmTime())
                .platform(member.getPlatform())
                .introStatus(member.getIntroStatus())
                .address(member.getAddress()) // 판매자 상세 주소
                .phoneNumber(member.getPhoneNumber()) // 판매자 휴대번호
                .kakaoId(member.getKakaoId()) // 판매자 카톡아이디
                .snsId(member.getSnsId()) // 판매자 SNS ID
                .storeName(member.getStoreName()) // 판매자 가게명
                .smartStoreLink(member.getSmartStoreLink()) // 스마트 스토어 주소
                .contentsTone(member.getContentsTone()) // 글 작성 톤
                .alarmFrequency(member.getAlarmFrequency())
                .build();
    }

    /**
     * 회원가입 요청을 위한 DTO (내부 클래스)
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRequestDTO {
        private String name;
        private String password;
        private String email;
        private String gender;
        private String nickname;
    }

    /**
     * 비밀번호 변경 요청 DTO (내부 클래스)
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PasswordRequestDTO {
        private String password;
        private String newPassword;
    }
}
