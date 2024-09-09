package ssafy.ddada.api.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.domain.member.player.command.UpdateProfileCommand;

public record MemberUpdateRequest (
        @Schema(description = "닉네임", example = "쿠잉비", minimum = "2", maximum = "20")
        String nickname,
        @Schema(description = "프로필 이미지 파일")
        MultipartFile profileImagePath
) {
    public UpdateProfileCommand toCommand() {
        return new UpdateProfileCommand(nickname, profileImagePath);
    }
}
