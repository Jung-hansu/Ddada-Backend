package ssafy.ddada.domain.member.courtadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.ddada.domain.member.courtadmin.entity.CourtAdmin;

import java.util.Optional;

public interface CourtAdminRepository extends JpaRepository<CourtAdmin, Long> {
    boolean existsByEmail(String email);
    Optional<CourtAdmin> findByEmail(String email);
}

