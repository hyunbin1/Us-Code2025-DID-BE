package nova.hackathon.member.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.hackathon.member.Member;
import nova.hackathon.member.MemberRepository;
import nova.hackathon.util.mail.EmailDTO;
import nova.hackathon.util.mail.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailAlarmScheduler {

    private final MemberRepository memberRepository;
    private final MailService mailService;

    // 매일 1분마다 실행 (운영 시 cron 수정 필요)
    @Scheduled(cron = "0 * * * * *") // 예시: 매분마다 실행
    public void sendScheduledEmails() {
        // 현재 시간 (시:분 기준으로 비교)
        LocalTime now = LocalTime.parse(LocalTime.now().withSecond(0).withNano(0).toString());
        // time 필드가 now와 일치하는 사용자에게 알림 전송
        List<Member> targetMembers = memberRepository.findByAlarmTime(now);
        for (Member member : targetMembers) {
            EmailDTO.Request req = EmailDTO.Request.builder()
                    .to(member.getEmail())
                    .subject("⏰ 리마인드 알림")
                    .content("안녕하세요! 설정한 시간에 맞춰 알림을 드립니다.")
                    .html(false)
                    .build();

            EmailDTO.Response res = mailService.send(req);
            log.info("[MAIL][{}] 전송 여부: {}", member.getEmail(), res.isSuccess());
        }
    }
}
