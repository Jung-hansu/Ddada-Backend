package ssafy.ddada.api.member.player.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.domain.member.player.command.UpdateProfileCommand;

public record PlayerUpdateRequest(
        @Schema(description = "닉네임", example = "쿠잉비")
        @Size(min = 2, max = 20)
        String nickname,

        @Schema(description = "프로필 이미지 파일")
        MultipartFile profileImagePath,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "한 줄 소개", example = "안녕하세요")
        @Size(max = 50)
        String description
) {
    public UpdateProfileCommand toCommand() {
        return new UpdateProfileCommand(nickname, profileImagePath, phoneNumber, description);
    }
}
