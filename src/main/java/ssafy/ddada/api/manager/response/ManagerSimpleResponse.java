package ssafy.ddada.api.manager.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.manager.entity.Manager;

@Schema(description = "매니저 정보 요약 응답 DTO")
public record ManagerSimpleResponse (
        @Schema(description = "매니저ID")
        Long id,
        @Schema(description = "매니저 별명")
        String nickname
){
    public static ManagerSimpleResponse from(Manager manager){
        return new ManagerSimpleResponse(manager.getId(), manager.getNickname());
    }
}
