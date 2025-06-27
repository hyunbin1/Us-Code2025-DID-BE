package nova.hackathon.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nova.hackathon.member.Member;
import nova.hackathon.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializer {
    private final MemberRepository memberRepository;

    @PostConstruct
    public void initData() {
        log.info("초기 더미 데이터 생성 시작...");

    }
}
