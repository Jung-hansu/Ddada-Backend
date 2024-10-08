package ssafy.ddada.api.member.manager.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.manager.entity.Manager;

@Schema(description = "매니저 세부 정보 조회 응답 DTO")
public record ManagerDetailResponse(
        @Schema(description = "매니저 ID")
        Long id,

        @Schema(description = "매니저 이메일")
        String email,

        @Schema(description = "매니저 별명")
        String nickname,

        @Schema(description = "매니저 전화번호")
        String phoneNumber,

        @Schema(description = "매니저 프로필 이미지")
        String profileImg
) {
    public static ManagerDetailResponse from(Manager manager){
        return new ManagerDetailResponse(
                manager.getId(),
                manager.getEmail(),
                manager.getNickname(),
                manager.getNumber(),
                manager.getProfileImg()
        );
    }
}
