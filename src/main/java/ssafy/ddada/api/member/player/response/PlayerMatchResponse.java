package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.Match;

import java.time.LocalDate;
import java.time.LocalTime;

public record PlayerMatchResponse(
        @Schema(description = "매치 ID")
        Long matchId,
        @Schema(description = "경기장 이름")
        String courtName,
        @Schema(description = "팀1 점수")
        Integer team1SetScore,
        @Schema(description = "팀2 점수")
        Integer team2SetScore,
        @Schema(description = "경기 날짜")
        LocalDate matchDate,
        @Schema(description = "경기 시간")
        LocalTime matchTime
) {
    public static PlayerMatchResponse from(Match match) {
        return new PlayerMatchResponse(
                match.getId(),
                match.getCourt().getName(),  // 필요한 데이터만 가져옴
                match.getTeam1SetScore(),
                match.getTeam2SetScore(),
                match.getMatchDate(),
                match.getMatchTime()
        );
    }
}
