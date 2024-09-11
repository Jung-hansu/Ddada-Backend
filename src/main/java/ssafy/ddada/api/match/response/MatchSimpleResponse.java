package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.api.member.court.response.CourtSimpleResponse;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.match.entity.MatchTime;

import java.time.LocalDate;

@Schema(description = "경기 정보 요약 응답 DTO")
public record MatchSimpleResponse(
        @Schema(description = "경기 ID")
        Long id,
        @Schema(description = "경기 일자")
        LocalDate date,
        @Schema(description = "경기 시간")
        MatchTime time,
        @Schema(description = "경기 상태")
        MatchStatus status,
        @Schema(description = "시설 정보")
        CourtSimpleResponse court
) {
        public static MatchSimpleResponse from(Match match){
                return new MatchSimpleResponse(
                        match.getId(),
                        match.getMatchDate(),
                        match.getMatchTime(),
                        match.getStatus(),
                        CourtSimpleResponse.from(match.getCourt())
                );
        }
}
