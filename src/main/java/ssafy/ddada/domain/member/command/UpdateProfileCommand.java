package ssafy.ddada.domain.member.command;

import org.springframework.web.multipart.MultipartFile;

public record UpdateProfileCommand(String nickname, MultipartFile profileImagePath) {}
