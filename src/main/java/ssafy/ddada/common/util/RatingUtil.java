package ssafy.ddada.common.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.member.player.entity.Player;

import java.util.List;

@Slf4j
@Component
public class RatingUtil {

    public static double calculateEloRating(int playerRating, int opponentRating, boolean isWin, int k) {
        // Elo 레이팅 변화량 계산
        double expectedScore = 1.0 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
        double actualScore = isWin ? 1.0 : 0.0;
        return k * (actualScore - expectedScore);
    }

    public static int calculateTeamRating(List<Player> team) {
        // 팀 평균 레이팅 계산
        return (int) Math.round(team.stream().mapToInt(Player::getRating).average().orElse(0));
    }

    public Integer updatePlayerRating(Player player, double opponentTeamRating, boolean isWin, Integer teamScore, int earnedRate, int missedRate) {
        int minChange = -100;
        int maxChange = 100;

        // 개인 성과를 반영한 레이팅 업데이트
        double ratingChange = calculateEloRating(player.getRating(), (int) opponentTeamRating, isWin, getDynamicK(player.getRating(), player));
        double performanceBonus = teamScore * 0.1;

        // 승리 시 레이팅 증가, 패배 시 감소
        ratingChange += isWin ? performanceBonus : -performanceBonus;

        // 변화량 제한
        ratingChange = Math.max(minChange, Math.min(maxChange, ratingChange));

        // 개인 득실 반영
        ratingChange += ratingChange * (earnedRate - 0.5);
        ratingChange -= ratingChange * (missedRate - 0.5);

        if (isWin) {
            // 이긴 경우: 연승 비율을 반영
            double winStreakRatio = (player.getWinStreak() > 0) ? (1 + (0.10 * player.getWinStreak())) : 1; // 연승에 따라 10%씩 증가
            ratingChange = ratingChange * winStreakRatio; // 연승 비율 적용
        } else {
            // 진 경우: 연패 비율을 반영
            double loseStreakRatio = (player.getLoseStreak() > 0) ? (1 - (0.10 * player.getLoseStreak())) : 1; // 연패에 따라 10%씩 감소
            ratingChange = ratingChange * loseStreakRatio; // 연패 비율 적용
        }

        // 새로운 레이팅 계산
        double newRating = player.getRating() + ratingChange;

        return (int) Math.round(newRating); // 최종적으로 int로 변환하여 반환
    }

    public static int getDynamicK(int playerRating, Player player) {
        int k = 0;
        if (playerRating < 1000) {
            k = 50; // 초보자 레벨에서는 큰 변화
        } else if (playerRating < 2000) {
            k = 40; // 중간 레벨에서는 보통의 변화
        } else {
            k = 30; // 고수 레벨에서는 작은 변화
        }
        if (player.getGameCount()<10) {
            k += 30;
        }
        return k;
    }
}
