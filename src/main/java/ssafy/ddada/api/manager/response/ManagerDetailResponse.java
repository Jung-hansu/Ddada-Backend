package ssafy.ddada.api.manager.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.entity.SearchedManager;

@Schema(description = "매니저 세부 정보 응답 DTO")
public record ManagerDetailResponse(
    @Schema(description = "매니저 ID", example = "1")
    SearchedManager manager
) {
    public static ManagerDetailResponse of(SearchedManager manager){
        return new ManagerDetailResponse(manager);
    }
}
