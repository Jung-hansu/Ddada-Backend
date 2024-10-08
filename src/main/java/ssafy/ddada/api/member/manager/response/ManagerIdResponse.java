package ssafy.ddada.api.member.manager.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "매니저 ID 응답 DTO")
public record ManagerIdResponse(
        Long id
){
    public static ManagerIdResponse of(Long id) {
        return new ManagerIdResponse(id);
    }
}
