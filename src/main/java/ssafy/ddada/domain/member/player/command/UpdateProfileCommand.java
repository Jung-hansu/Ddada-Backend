package ssafy.ddada.domain.member.player.command;

import org.springframework.web.multipart.MultipartFile;

public record UpdateProfileCommand(
        String nickname,
        MultipartFile profileImagePath,
        String phoneNumber,
        String description) {}
