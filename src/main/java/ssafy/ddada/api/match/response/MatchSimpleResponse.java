package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.match.entity.MatchType;
import ssafy.ddada.domain.match.entity.RankType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        @Schema(description = "랭크 타입")
        RankType rankType,
        @Schema(description = "경기 타입")
        MatchType matchType,
        @Schema(description = "경기 평균 레이팅")
        Integer rating,
        @Schema(description = "팀1 인원 수")
        Integer team1PlayerCount,
        @Schema(description = "팀2 인원 수")
        Integer team2PlayerCount,
        @Schema(description = "팀1 성별")
        List<String> team1Gender,
        @Schema(description = "팀2 성별")
        List<String> team2Gender,
        @Schema(description = "예약 여부")
        boolean isReserved,
        @Schema(description = "코트 정보")
        CourtSimpleResponse court
) {
        public static MatchSimpleResponse from(Match match, boolean isReserved){
                int team1PlayerCount = match.getTeam1().getPlayerCount();
                int team2PlayerCount = match.getTeam2().getPlayerCount();
                int team1Rating = match.getTeam1().getRating();
                int team2Rating = match.getTeam2().getRating();
                int rating = (team1Rating * team1PlayerCount + team2Rating * team2PlayerCount) /
                        (team1PlayerCount + team2PlayerCount);
                List<String> team1Gender = match.getTeamGender(match.getTeam1());
                List<String> team2Gender = match.getTeamGender(match.getTeam2());

                return new MatchSimpleResponse(
                        match.getId(),
                        match.getMatchDate(),
                        match.getMatchTime(),
                        match.getStatus(),
                        match.getRankType(),
                        match.getMatchType(),
                        rating,
                        team1PlayerCount,
                        team2PlayerCount,
                        team1Gender,
                        team2Gender,
                        isReserved,
                        CourtSimpleResponse.onMatchListFrom(match.getCourt())
                );
        }
}
