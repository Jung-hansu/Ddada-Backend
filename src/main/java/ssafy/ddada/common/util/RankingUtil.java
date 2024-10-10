package ssafy.ddada.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.member.player.entity.Player;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PLAYER_RANKING_KEY = "player_ranking";

    // 플레이어의 rating을 redis에 저장 (ZSet 사용)
    public void savePlayerToRanking(Player player) {
        redisTemplate.opsForZSet().add(PLAYER_RANKING_KEY, player.getNickname(), player.getRating());
    }

    // 플레이어 랭킹에서 제거
    public void removePlayerFromRanking(Player player) {
        redisTemplate.opsForZSet().remove(PLAYER_RANKING_KEY, player.getNickname());
    }

    public void updatePlayerRating(Player player) {
        removePlayerFromRanking(player);
        savePlayerToRanking(player);
    }

    // 특정 플레이어의 현재 랭킹을 가져오기
    public Long getPlayerRank(Player player) {
        Long rank = redisTemplate.opsForZSet().reverseRank(PLAYER_RANKING_KEY, player.getNickname());
        return rank == null ? -1 : rank + 1; // 0-based index이므로 1을 더함
    }

    public Set<ZSetOperations.TypedTuple<String>> getAllPlayers() {
        // ZSet에서 nickname과 rating을 함께 가져오기
        return redisTemplate.opsForZSet().reverseRangeWithScores(PLAYER_RANKING_KEY, 0, -1);
    }

}

