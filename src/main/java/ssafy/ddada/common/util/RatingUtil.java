package ssafy.ddada.common.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.member.player.entity.Player;

import java.util.List;

@Slf4j
@Component
public class RatingUtil {

    // 티어 설정
    private static final String[] TIERS = {
            "Unranked", "Amateur I", "Amateur II", "Amateur III",
            "Semi-Pro I", "Semi-Pro II", "Semi-Pro III",
            "Professional I", "Professional II", "Professional III", "Master"
    };

    public static String getTier(int rating) {
        // 레이팅에 따른 티어 결정
        for (int i = TIERS.length - 1; i >= 0; i--) {
            if (rating >= i * 500) {  // 각 티어의 기준 레이팅을 정의할 수 있습니다.
                return TIERS[i];
            }
        }
        return "Unranked";
    }

    public static double calculateEloRating(int playerRating, int opponentRating, boolean isWin, int k) {
        // Elo 레이팅 변화량 계산
        double expectedScore = 1.0 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
        double actualScore = isWin ? 1.0 : 0.0;
        return k * (actualScore - expectedScore);
    }

    public static double calculateTeamRating(List<Player> team) {
        // 팀 평균 레이팅 계산
        return team.stream().mapToInt(Player::getRating).average().orElse(0);
    }

    public Integer updatePlayerRating(Player player, double opponentTeamRating, boolean isWin, int personalScore, int k, int minChange, int maxChange) {
        // 개인 성과를 반영한 레이팅 업데이트
        double ratingChange = calculateEloRating(player.getRating(), (int) opponentTeamRating, isWin, k);
        double performanceBonus = personalScore * 0.1;

        // 승리 시 레이팅 증가, 패배 시 감소
        ratingChange += isWin ? performanceBonus : -performanceBonus;

        // 변화량 제한
        ratingChange = Math.max(minChange, Math.min(maxChange, ratingChange));

        // 새로운 레이팅 계산
        int newRating = (int) Math.round(player.getRating() + ratingChange);
        log.info("Player {}'s rating updated: {} -> {}, {}", player.getId(), player.getRating(), newRating, ratingChange);
        return newRating;
    }
}
