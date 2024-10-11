package ssafy.ddada.domain.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.match.repository.MatchRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpiredMatchScheduler {

    private final MatchRepository matchRepository;

    // 매일 오전 4시에 스케줄러 실행
    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void collectOutDatedMatches() {
        log.info("미진행 경기 취소 프로세스 실행");
        try {
            List<Match> outDatedMatches = matchRepository.findAllOutDatedMatches();

            if (outDatedMatches.isEmpty()) {
                log.info("취소할 미진행 경기가 없습니다.");
                return;
            }

            for (Match match : outDatedMatches) {
                match.setStatus(MatchStatus.CANCELED);
            }
            matchRepository.saveAll(outDatedMatches);
            log.info("미진행 경기 {}개 취소 완료", outDatedMatches.size());

        } catch (Exception e) {
            log.error("미진행 경기 취소 프로세스 비정상 종료: {}", e.getMessage(), e);
            throw e; // 트랜잭션 롤백
        }
        log.info("미진행 경기 취소 프로세스 정상 종료");
    }

}
