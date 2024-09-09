package ssafy.ddada.domain.member.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.ddada.domain.member.common.Player;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    boolean existsByEmail(String email);
    Optional<Player> findByEmail(String email);
    boolean existsByNickname(String nickname);
}
