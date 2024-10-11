package ssafy.ddada.domain.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpiredMemberScheduler {

    private final PlayerRepository playerRepository;

    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void collectDeletedMembers() {
        log.info("탈퇴한 유저 삭제 프로세스 실행");
        try {
            List<Player> deletedPlayers = playerRepository.findDeletedPlayers();
            playerRepository.deleteAll(deletedPlayers);
        } catch (Exception e) {
            log.error("탈퇴한 유저 삭제 프로세스 비정상 종료: {}", e.getMessage(), e);
            throw e; // 트랜잭션 롤백
        }
        log.info("탈퇴한 유저 삭제 프로세스 정상 종료");
    }

}
