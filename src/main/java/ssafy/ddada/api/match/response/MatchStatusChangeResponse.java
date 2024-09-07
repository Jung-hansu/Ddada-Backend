package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.MatchStatus;

@Schema(description = "경기 상태 전환 응답 DTO")
public record MatchStatusChangeResponse (
    @Schema(description = "경기 상태", example = "진행중")
    MatchStatus status,
    @Schema(description = "요청 성공 메세지", example = "경기 상태가 성공적으로 전환되었습니다.")
    String message
) {
    public static MatchStatusChangeResponse of(MatchStatus status){
        return new MatchStatusChangeResponse(status, "경기 상태가 성공적으로 전환되었습니다.");
    }
}
