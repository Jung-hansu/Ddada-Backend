package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "경기 생성 응답 DTO")
public record MatchCreateResponse (
    @Schema(description = "경기 생성 성공 메세지", example = "경기가 성공적으로 생성되었습니다.")
    String message
) {
    public static MatchCreateResponse of() {
        return new MatchCreateResponse("경기가 성공적으로 생성되었습니다.");
    }
}
