package ssafy.ddada.api.member.manager.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.manager.entity.Manager;

@Schema(description = "매니저 정보 요약 응답 DTO")
public record ManagerSimpleResponse (
        @Schema(description = "매니저 ID")
        Long id,

        @Schema(description = "매니저 별명")
        String nickname,

        @Schema(description = "매니저 이미지")
        String image
){
    public static ManagerSimpleResponse from(Manager manager){
        if (manager == null) {
            return null;
        }
        return new ManagerSimpleResponse(manager.getId(), manager.getNickname(), manager.getProfileImg());
    }
}
