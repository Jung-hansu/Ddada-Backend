package ssafy.ddada.domain.member.manager.repository;


import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssafy.ddada.domain.member.manager.entity.Manager;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    @Query("""
        SELECT mg
        FROM Manager mg
        WHERE mg.email = :email AND mg.isDeleted = false
    """)
    Optional<Manager> findByEmail(@Param("email") String email);

}
