package ssafy.ddada.api.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteMemberResponse(
        @Schema(description = "메시지", example = "회원 탈퇴가 성공적으로 처리되었습니다.")
        String message
) {
    public static DeleteMemberResponse of(String message) {
        return new DeleteMemberResponse(message);
    }
}
