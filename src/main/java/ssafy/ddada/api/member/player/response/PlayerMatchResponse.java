package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.Match;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "선수가 할당된 경기 응답 DTO")
public record PlayerMatchResponse(
        @Schema(description = "매치 ID")
        Long matchId,

        @Schema(description = "경기장 이름")
        String gymName,

        @Schema(description = "코트 주소")
        String gymAddress,

        @Schema(description = "경기 날짜")
        LocalDate matchDate,

        @Schema(description = "경기 시간")
        LocalTime matchTime,

        @Schema(description = "경기 타입")
        String matchType,

        @Schema(description = "경기 랭크/친선")
        String rankType,

        @Schema(description = "평균 레이팅")
        Integer avgRating,

        @Schema(description = "경기 예약 상태")
        String matchStatus,

        @Schema(description = "나의 포지션")
        String MyTeamAndNumber,

        @Schema(description = "나의 변화 레이팅")
        Integer myRatingChange

) {
    public static PlayerMatchResponse from(Match match, Integer avgRating, String myTeamAndNumber, Integer myRatingChange) {
        return new PlayerMatchResponse(
                match.getId(),
                match.getCourt().getGym().getName(),
                match.getCourt().getGym().getAddress(),
                match.getMatchDate(),
                match.getMatchTime(),
                match.getMatchType().name(),
                match.getRankType().name(),
                avgRating,
                match.getStatus().name(),
                myTeamAndNumber,
                myRatingChange
        );
    }
}
