package ssafy.ddada.domain.member.manager.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.ddada.domain.member.manager.entity.Manager;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    boolean existsByEmail(String email);
    Optional<Manager> findByEmail(String email);

}
