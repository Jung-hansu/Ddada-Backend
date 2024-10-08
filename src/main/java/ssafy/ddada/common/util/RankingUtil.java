package ssafy.ddada.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.member.player.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PLAYER_RANKING_KEY = "player_ranking";

    // 플레이어의 rating을 redis에 저장 (ZSet 사용)
    public void savePlayerToRanking(Player player) {
        redisTemplate.opsForZSet().add(PLAYER_RANKING_KEY, player.getNickname(), player.getRating());
    }

    // 플레이어 랭킹에서 제거
    public void removePlayerFromRanking(Player player) {
        redisTemplate.opsForZSet().remove(PLAYER_RANKING_KEY, player.getId());
    }

    public void updatePlayerRating(Player player) {
        redisTemplate.opsForZSet().remove(PLAYER_RANKING_KEY, player.getId());
        redisTemplate.opsForZSet().add(PLAYER_RANKING_KEY, player.getNickname(), player.getRating());
    }

    // 특정 플레이어의 현재 랭킹을 가져오기
    public Long getPlayerRank(String nickName) {
        Long rank = redisTemplate.opsForZSet().reverseRank(PLAYER_RANKING_KEY, nickName);
        return rank == null ? -1 : rank + 1; // 0-based index이므로 1을 더함
    }

    public List<List<Object>> getAllPlayers() {
        // ZSet에서 nickname과 rating을 함께 가져오기
        Set<ZSetOperations.TypedTuple<Object>> playerData = redisTemplate.opsForZSet().reverseRangeWithScores(PLAYER_RANKING_KEY, 0, -1);

        // 가져온 데이터를 2중 리스트로 변환
        return playerData.stream()
                .map(tuple -> Arrays.asList(tuple.getValue(), tuple.getScore()))
                .collect(Collectors.toList());
    }

}

