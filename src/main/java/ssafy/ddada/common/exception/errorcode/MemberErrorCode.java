package ssafy.ddada.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    DUPLICATE_EMAIL(400, "MEMBER_400_1", "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(400, "MEMBER_400_2", "이미 존재하는 닉네임입니다."),
    INVALID_PASSWORD(400, "MEMBER_400_3", "비밀번호가 일치하지 않습니다."),
    INVALID_EMAIL(400, "MEMBER_400_4", "이메일 형식이 올바르지 않습니다."),
    INVALID_NICKNAME(400, "MEMBER_400_5", "닉네임 형식이 올바르지 않습니다."),
    INVALID_NAME(400, "MEMBER_400_6", "이름 형식이 올바르지 않습니다."),
    DUPLICATE_MEMBER(400, "MEMBER_400_7", "이미 존재하는 회원입니다."),
    NOT_ALLOWED_EXTENSION(400, "MEMBER_400_8", "프로필 이미지의 확장자는 jpg, jpeg, png, gif만 가능합니다."),
    IMAGE_TOO_LARGE(400, "MEMBER_400_9", "업로드할 이미지 용량이 2MB를 초과합니다."),

    INVALID_LOGIN_INFO(401, "MEMBER_401_1", "로그인 유저가 존재하지 않습니다."),

    NOT_FOUND_MEMBER(404, "MEMBER_404_1", "존재하지 않는 회원입니다."),
    NOT_FOUND_TEMP_MEMBER(404, "MEMBER_404_2", "로그인 과정에서 생성된 임시 회원 정보가 존재하지 않습니다."),
    PROFILE_NOT_FOUND(404, "MEMBER_404_6", "프로필 이미지를 찾을 수 없습니다."),
    PROFILE_NOT_FOUND_IN_S3(404, "MEMBER_404_7", "S3에 프로필 이미지를 찾을 수 없습니다."),

    ALREADY_DELETED_MEMBER(409, "MEMBER_409_1", "이미 삭제된 회원입니다."),

    ABNORMAL_LOGIN_PROGRESS(500, "MEMBER_500_1", "비정상적으로 로그인이 진행되었습니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
