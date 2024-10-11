package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "선수 랭킹 응답 DTO")
public record PlayerRankingResponse(
        @Schema(description = "전체 랭킹")
        List<PlayerRanking> rankings
) {
    public record PlayerRanking(
            @Schema(description = "선수 랭킹", example = "1")
            Integer ranking,

            @Schema(description = "선수 닉네임", example = "JohnDoe")
            String nickname,

            @Schema(description = "선수 점수", example = "1500")
            Integer rating,

            @Schema(description = "플레이어 경기 수", example = "2")
            Integer playCount
    ) {
        public static PlayerRanking of(Integer ranking, String nickname, Integer rating, Integer playCount) {
            return new PlayerRanking(ranking, nickname, rating, playCount);
        }
    }

    public static PlayerRankingResponse of (List<PlayerRanking> rankings) {
        return new PlayerRankingResponse(rankings);
    }

}
