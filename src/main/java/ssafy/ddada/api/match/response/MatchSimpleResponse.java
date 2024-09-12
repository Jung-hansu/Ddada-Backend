package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.match.entity.MatchType;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "경기 정보 요약 응답 DTO")
public record MatchSimpleResponse(
        @Schema(description = "경기 ID")
        Long id,
        @Schema(description = "경기 일자")
        LocalDate date,
        @Schema(description = "경기 시간")
        LocalTime time,
        @Schema(description = "경기 상태")
        MatchStatus status,
        @Schema(description = "경기 타입")
        MatchType type,
        @Schema(description = "경기 평균 레이팅")
        Integer rating,
        @Schema(description = "팀1 인원 수")
        Integer team1PlayerCount,
        @Schema(description = "팀2 인원 수")
        Integer team2PlayerCount,
        @Schema(description = "시설 정보")
        CourtSimpleResponse court
) {
        public static MatchSimpleResponse from(Match match){
                int team1PlayerCount = match.getTeam1().getPlayerCount();
                int team2PlayerCount = match.getTeam2().getPlayerCount();
                int team1Rating = match.getTeam1().getRating();
                int team2Rating = match.getTeam2().getRating();
                int rating = (team1Rating * team1PlayerCount + team2Rating * team2PlayerCount) /
                        (team1PlayerCount + team2PlayerCount);

                return new MatchSimpleResponse(
                        match.getId(),
                        match.getMatchDate(),
                        match.getMatchTime(),
                        match.getStatus(),
                        match.getMatchType(),
                        rating,
                        team1PlayerCount,
                        team2PlayerCount,
                        CourtSimpleResponse.from(match.getCourt())
                );
        }
}
