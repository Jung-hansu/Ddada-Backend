package ssafy.ddada.api.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.domain.member.command.UpdateProfileCommand;

public record UpdateProfileRequest(
        @Schema(description = "닉네임", example = "쿠잉비", minimum = "2", maximum = "20")
        @Size(min = 2, max = 20)
        String nickName,

        @Schema(description = "프로필 이미지 파일")
        MultipartFile profileImagePath

) {
    public UpdateProfileCommand toCommand() {
        return new UpdateProfileCommand(nickName, profileImagePath);
    }
}
