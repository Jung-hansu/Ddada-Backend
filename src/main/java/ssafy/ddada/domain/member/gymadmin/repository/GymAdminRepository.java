package ssafy.ddada.domain.member.gymadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;

import java.util.Optional;

public interface GymAdminRepository extends JpaRepository<GymAdmin, Long> {
    boolean existsByEmail(String email);
    Optional<GymAdmin> findByEmail(String email);
}

