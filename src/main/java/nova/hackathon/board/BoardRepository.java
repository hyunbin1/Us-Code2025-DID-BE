package nova.hackathon.board;

import nova.hackathon.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT MAX(b.ver) FROM Board b WHERE b.member = :member AND b.type = :type")
    Optional<Integer> findMaxVersionByMemberAndType(@Param("member") Member member, @Param("type") Member.Platform type);
    List<Board> findAllByMemberOrderByVerDescIdAsc(Member member);

}
