package ssafy.ddada.api.match.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.member.manager.response.ManagerSimpleResponse;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.match.entity.MatchType;
import ssafy.ddada.domain.match.entity.RankType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Schema(description = "경기 세부 정보 응답 DTO")
public record MatchDetailResponse(
        @Schema(description = "경기 ID")
        Long id,
        @Schema(description = "코트")
        CourtDetailResponse court,
        @Schema(description = "팀1")
        TeamDetailResponse team1,
        @Schema(description = "팀2")
        TeamDetailResponse team2,
        @Schema(description = "담당 매니저")
        ManagerSimpleResponse manager,
        @Schema(description = "승리 팀 번호")
        Integer winnerTeamNumber,
        @Schema(description = "팀1의 세트 점수")
        Integer team1SetScore,
        @Schema(description = "팀2의 세트 점수")
        Integer team2SetScore,
        @Schema(description = "경기 상태")
        MatchStatus status,
        @Schema(description = "랭크 타입")
        RankType rankType,
        @Schema(description = "경기 타입")
        MatchType matchType,
        @Schema(description = "경기 일자")
        LocalDate date,
        @Schema(description = "경기 시간")
        LocalTime time,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(description = "세트 정보 요약 리스트")
        List<SetSimpleResponse> sets
) {

    public static MatchDetailResponse from(Match match,String GymImage, String Team1Player1Image, String Team1Player2Image, String Team2Player1Image, String Team2Player2Image) {
        return new MatchDetailResponse(
                match.getId(),
                CourtDetailResponse.fromWhereMatchDetail(match.getCourt(), GymImage),
                TeamDetailResponse.from(match.getTeam1(), Team1Player1Image, Team1Player2Image),
                TeamDetailResponse.from(match.getTeam2(), Team2Player1Image, Team2Player2Image),
                ManagerSimpleResponse.from(match.getManager()),
                match.getWinnerTeamNumber(),
                match.getTeam1SetScore(),
                match.getTeam2SetScore(),
                match.getStatus(),
                match.getRankType(),
                match.getMatchType(),
                match.getMatchDate(),
                match.getMatchTime(),
                match.getSets()
                        .stream()
                        .map(SetSimpleResponse::from)
                        .sorted(Comparator.comparing(SetSimpleResponse::setNumber))
                        .toList()
        );
    }

}
