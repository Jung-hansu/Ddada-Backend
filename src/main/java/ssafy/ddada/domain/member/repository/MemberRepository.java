package ssafy.ddada.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.ddada.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    boolean existsBynickname(String nickname);
}