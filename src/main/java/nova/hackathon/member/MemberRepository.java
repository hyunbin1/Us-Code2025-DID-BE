package nova.hackathon.member;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUuid(UUID uuid);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    boolean existsByNickname(String nickname);
    List<Member> findByAlarmTime(LocalDateTime alarmTime);

}
