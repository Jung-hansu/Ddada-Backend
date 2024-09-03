package ssafy.ddada.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.ddada.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
