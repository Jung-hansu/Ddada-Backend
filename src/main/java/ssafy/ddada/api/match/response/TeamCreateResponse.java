package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 생성 응답 DTO")
public record TeamCreateResponse(
        @Schema(description = "생성된 팀의 ID")
        Long id,
        @Schema(description = "팀 생성 성공 메세지", example = "팀이 성공적으로 생성되었습니다.")
        String message
) {
    public static TeamCreateResponse of(Long id){
        return new TeamCreateResponse(id, "팀이 성공적으로 생성되었습니다.");
    }
}
