package nova.hackathon.member.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.hackathon.member.Member;
import nova.hackathon.member.Member.AlarmFrequency;
import nova.hackathon.member.MemberRepository;
import nova.hackathon.util.mail.EmailDTO;
import nova.hackathon.util.mail.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailAlarmScheduler {

    private final MemberRepository memberRepository;
    private final MailService mailService;

    /**
     * 매 분마다 실행되며, 요일 + 시간 조건을 만족하는 회원에게만 이메일 알림을 전송합니다.
     * 알림 발송 조건:
     * - ONCE: 일요일
     * - TWICE: 화요일, 토요일
     * - THIRD: 월요일, 수요일, 금요일
     */
    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void sendScheduledEmails() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime().withSecond(0).withNano(0);

        // 현재 시간에 해당하는 회원 조회
        List<Member> candidates = memberRepository.findByAlarmTime(currentTime);
        log.info("[MAIL-SCHEDULER] {} 요일 {} 시각 기준 후보자 수: {}", today, currentTime, candidates.size());

        for (Member member : candidates) {
            AlarmFrequency frequency = member.getAlarmFrequency();

            // null 방지 및 요일 조건 만족 시 발송
            if (frequency != null && shouldSendToday(today, frequency)) {
                EmailDTO.Request req = EmailDTO.Request.builder()
                        .to(member.getEmail())
                        .subject("⏰ 리마인드 알림")
                        .content("안녕하세요! 설정한 시간에 맞춰 알림을 드립니다.")
                        .html(false)
                        .build();

                EmailDTO.Response res = mailService.send(req);
                log.info("[MAIL] {} 전송 결과: {}", member.getEmail(), res.isSuccess());
            }
        }
    }

    /**
     * 요일과 빈도 조건에 따라 오늘 발송 대상인지 여부 반환
     */
    private boolean shouldSendToday(DayOfWeek today, AlarmFrequency frequency) {
        return switch (frequency) {
            case ONCE -> today == DayOfWeek.SUNDAY;
            case TWICE -> EnumSet.of(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY).contains(today);
            case THIRD -> EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY).contains(today);
        };
    }
}
