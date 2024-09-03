package ssafy.ddada.domain.member.command;

import org.springframework.web.multipart.MultipartFile;

public record UpdateProfileCommand(String nickName, MultipartFile profileImagePath) {}
