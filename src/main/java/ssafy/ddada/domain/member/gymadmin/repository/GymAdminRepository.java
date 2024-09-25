package ssafy.ddada.domain.member.gymadmin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;

import java.util.Optional;

public interface GymAdminRepository extends JpaRepository<GymAdmin, Long> {
    @EntityGraph(attributePaths = {"gym"})
    @Query("""
        SELECT ga
        FROM GymAdmin ga
        WHERE ga.id = :gymAdminId
    """)
    Optional<GymAdmin> findByIdWithInfos(Long gymAdminId);
    boolean existsByEmail(String email);
    Optional<GymAdmin> findByEmail(String email);
}

