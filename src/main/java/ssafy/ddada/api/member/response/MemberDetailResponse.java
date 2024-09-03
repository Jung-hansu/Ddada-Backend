package ssafy.ddada.api.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.entity.Gender;

public record MemberDetailResponse(
        @Schema(description = "프로필 이미지 base64", example = "https://example.com/profile.jpg")
        String profileImageBase64,

        @Schema(description = "닉네임", example = "쿠잉비")
        String nickName,

        @Schema(description = "성별", example = "MALE")
        String gender,

        @Schema(description = "방 존재 여부", example = "true")
        Boolean roomExist
) {
    public static MemberDetailResponse of(String profileImageBase64, String nickName, Gender gender, Boolean roomExist) {
        return new MemberDetailResponse(profileImageBase64, nickName, gender.getValue(), roomExist);
    }
}
