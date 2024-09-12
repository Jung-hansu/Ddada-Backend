package ssafy.ddada.api.match.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.api.member.manager.response.ManagerSimpleResponse;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.match.entity.MatchTime;
import ssafy.ddada.domain.match.entity.MatchType;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "경기 세부 정보 응답 DTO")
public record MatchDetailResponse(
        @Schema(description = "경기 ID")
        Long id,
        @Schema(description = "시설")
        CourtSimpleResponse court,
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
        @Schema(description = "단복식 여부")
        MatchType type,
        @Schema(description = "경기 일자")
        LocalDate date,
        @Schema(description = "경기 시간")
        MatchTime time,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(description = "세트 정보 요약 리스트")
        List<SetSimpleResponse> sets
) {

    public static MatchDetailResponse from(Match match){
        return new MatchDetailResponse(
                match.getId(),
                CourtSimpleResponse.from(match.getCourt()),
                TeamDetailResponse.from(match.getTeam1()),
                TeamDetailResponse.from(match.getTeam2()),
                ManagerSimpleResponse.from(match.getManager()),
                match.getWinnerTeamNumber(),
                match.getTeam1SetScore(),
                match.getTeam2SetScore(),
                match.getStatus(),
                match.getMatchType(),
                match.getMatchDate(),
                match.getMatchTime(),
                match.getSets()
                        .stream()
                        .map(SetSimpleResponse::from)
                        .toList()
        );
    }

}
