package ssafy.ddada.api.gym.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.api.member.manager.response.ManagerSimpleResponse;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "체육관 경기 리스트 응답 DTO")
public record GymMatchesResponse(
        @Schema(description = "경기 리스트")
        List<GymMatchResponse> matches
) {
    public record GymMatchResponse(
            @Schema(description = "경기 ID")
            Long id,
            @Schema(description = "경기 일자")
            LocalDate date,
            @Schema(description = "경기 시간")
            LocalTime time,
            @Schema(description = "코트 번호")
            Integer courtNumber,
            @Schema(description = "선수 인원수")
            Integer playerCount,
            @Schema(description = "경기 상태")
            MatchStatus status,
            @Schema(description = "매니저")
            ManagerSimpleResponse manager
    ) {
        public static GymMatchResponse from(Match match) {
            return new GymMatchResponse(
                    match.getId(),
                    match.getMatchDate(),
                    match.getMatchTime(),
                    match.getCourt().getCourtNumber(),
                    match.getTeam1().getPlayerCount() + match.getTeam2().getPlayerCount(),
                    match.getStatus(),
                    ManagerSimpleResponse.from(match.getManager())
            );
        }
    }

    public static GymMatchesResponse from(List<Match> matches) {
        return new GymMatchesResponse(
                matches.stream()
                        .map(GymMatchResponse::from)
                        .toList()
        );
    }
}
