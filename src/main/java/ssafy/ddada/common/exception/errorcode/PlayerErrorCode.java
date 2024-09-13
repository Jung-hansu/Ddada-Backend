package ssafy.ddada.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ssafy.ddada.api.StatusCode.*;

@Getter
@AllArgsConstructor
public enum PlayerErrorCode implements BaseErrorCode {
    EMAIL_DUPLICATE(BAD_REQUEST, "MEMBER_400_1", "이미 존재하는 이메일입니다."),
    NICKNAME_DUPLICATE(BAD_REQUEST, "MEMBER_400_2", "이미 존재하는 닉네임입니다."),
    INVALID_PASSWORD(BAD_REQUEST, "MEMBER_400_3", "비밀번호가 일치하지 않습니다."),
    INVALID_EMAIL(BAD_REQUEST, "MEMBER_400_4", "이메일 형식이 올바르지 않습니다."),
    INVALID_NICKNAME(BAD_REQUEST, "MEMBER_400_5", "닉네임 형식이 올바르지 않습니다."),
    INVALID_NAME(BAD_REQUEST, "MEMBER_400_6", "이름 형식이 올바르지 않습니다."),
    MEMBER_DUPLICATE(BAD_REQUEST, "MEMBER_400_7", "이미 존재하는 회원입니다."),
    EXTENSION_NOT_ALLOWED(BAD_REQUEST, "MEMBER_400_8", "프로필 이미지의 확장자는 jpg, jpeg, png, gif만 가능합니다."),
    IMAGE_TOO_LARGE(BAD_REQUEST, "MEMBER_400_9", "업로드할 이미지 용량이 2MB를 초과합니다."),
    MESSAGE_SENDING(BAD_REQUEST, "MEMBER_400_10", "메시지 전송에 실패하였습니다."),
    LOGIN_INFO_INVALID(UNAUTHORIZED, "MEMBER_401_1", "로그인 유저가 존재하지 않습니다."),

    MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER_404_1", "존재하지 않는 회원입니다."),
    TEMP_MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER_404_2", "로그인 과정에서 생성된 임시 회원 정보가 존재하지 않습니다."),
    PROFILE_NOT_FOUND(NOT_FOUND, "MEMBER_404_3", "프로필 이미지를 찾을 수 없습니다."),
    PROFILE_NOT_FOUND_IN_S3(NOT_FOUND, "MEMBER_404_4", "S3에 프로필 이미지를 찾을 수 없습니다."),
    VERIFICATION_FAILURE(NOT_FOUND, "MEMBER_404_5", "인증에 실패하였습니다."),
    EMAIL_NOT_FOUND(NOT_FOUND, "MEMBER_404_6", "존재하지 않는 이메일입니다."),
    PASSWORD_NOT_MATCH(NOT_FOUND, "MEMBER_404_7", "비밀번호가 일치하지 않습니다."),
    PHONE_NUMBER_NOT_FOUND(NOT_FOUND, "MEMBER_404_8", "존재하지 않는 전화번호입니다."),
    KAKAO_MAIL_PLAYER_NOT_FOUND(NOT_FOUND, "MEMBER_404_9", "카카오 메일에 해당하는 회원을 찾을 수 없습니다."),

    MEMBER_ALREADY_DELETED(CONFLICT, "MEMBER_409_1", "이미 삭제된 회원입니다."),

    ABNORMAL_LOGIN_PROGRESS(INTERNAL_SERVER_ERROR, "MEMBER_500_1", "비정상적으로 로그인이 진행되었습니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
