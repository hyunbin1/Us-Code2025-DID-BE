package nova.hackathon.member;

import jakarta.persistence.*;
import lombok.*;
import nova.hackathon.util.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Entity
@Entity
@Table(name = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING) // `role` 필드 추가
    @Column(nullable = false)
    private Role role;  // Role enum 타입으로 설정

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String nickname;
    private LocalDateTime alarmTime;

    private Boolean introStatus; // 처음 프로필 설정 여부

    private String contactEmail; // 컨텍용 이매일
    private String address; // 판매자 상세 주소
    private String phoneNumber; // 판매자 휴대번호
    private String kakaoId; // 판매자 카톡아이디
    private String snsId; // 판매자 SNS ID
    private String smartStoreLink; // 스마트 스토어 주소\

    @Enumerated(EnumType.STRING)
    private ContentsTone contentsTone;

    private String storeName;


    @ElementCollection(targetClass = Platform.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_platform", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "platform", nullable = false)
    private List<Platform> platform;

    public enum Gender {
        MALE, FEMALE, OTHERS;
        public static Gender fromString(String value) {
            try {
                return Gender.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new IllegalArgumentException("Invalid gender value: " + value);
            }
        }
    }

    public enum Platform {
        NAVER_BLOG, INSTAGRAM, NAVER_STORE, CARROT;
        public static Platform fromString(String value) {
            try {
                return Platform.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new IllegalArgumentException("Invalid Platform value: " + value);
            }
        }
    }

    @Getter
    public enum ContentsTone {
        SNS_CASUAL("SNS 캐주얼 톤"),
        FRIENDLY("정겨운 톤"),
        LIVELY("발랄한 톤"),
        CALM_EXPLANATORY("차분한 설명 톤");

        private final String description;

        ContentsTone(String description) {
            this.description = description;
        }

        public static ContentsTone fromString(String value) {
            try {
                return ContentsTone.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new IllegalArgumentException("Invalid ContentsTone value: " + value);
            }
        }
    }


    public enum Role {
        USER, ADMIN, DEVELOPER
    }

    public static Member create(MemberDTO.MemberRequestDTO memberDTO, String encodePassword) {
        return Member.builder()
                .uuid(UUID.randomUUID()) // UUID 자동 생성
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .password(encodePassword)
                .gender(Gender.fromString(memberDTO.getGender())) // 대소문자 변환
                .nickname(memberDTO.getNickname())
                .introStatus(Boolean.FALSE)
                .role(Role.USER)
                .build();
    }



    public void update(MemberDTO memberDTO) {
        // ── 단순 필드
        this.name           = getOrDefault(memberDTO.getName(),           this.name);
        this.email          = getOrDefault(memberDTO.getEmail(),          this.email);
        this.contactEmail   = getOrDefault(memberDTO.getContactEmail(),         this.contactEmail);
        this.nickname       = getOrDefault(memberDTO.getNickname(),       this.nickname);
        this.storeName       = getOrDefault(memberDTO.getStoreName(),       this.storeName);
        this.introStatus    = getOrDefault(memberDTO.getIntroStatus(),    this.introStatus);
        this.address        = getOrDefault(memberDTO.getAddress(),        this.address);
        this.kakaoId        = getOrDefault(memberDTO.getKakaoId(),        this.kakaoId);
        this.phoneNumber    = getOrDefault(memberDTO.getPhoneNumber(),    this.phoneNumber);
        this.storeName    = getOrDefault(memberDTO.getStoreName(),    this.storeName);
        this.snsId          = getOrDefault(memberDTO.getSnsId(),          this.snsId);
        this.smartStoreLink = getOrDefault(memberDTO.getSmartStoreLink(), this.smartStoreLink);

        // ── Enum 필드
        if (memberDTO.getGender() != null) {
            this.gender = Gender.fromString(memberDTO.getGender());
        }
        if (memberDTO.getContentsTone() != null) {
            this.contentsTone = ContentsTone.fromString(String.valueOf(memberDTO.getContentsTone()));
        }

        // ── 컬렉션 필드 (플랫폼)
        if (memberDTO.getPlatform() != null) {
            this.platform.clear();                 // 기존 값 제거
            this.platform.addAll(memberDTO.getPlatform()); // 새 값 추가
        }
    }


    public void updatePassword(String encodedNewPassword) {
        this.password = encodedNewPassword;
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }
}
