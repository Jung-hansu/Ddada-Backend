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
            Integer rating
    ) {
        public static PlayerRanking of(Integer ranking, String nickname, Integer rating) {
            return new PlayerRanking(ranking, nickname, rating);
        }
    }

    public static PlayerRankingResponse of (List<PlayerRanking> rankings) {
        return new PlayerRankingResponse(rankings);
    }

}
