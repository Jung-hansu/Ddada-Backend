package ssafy.ddada.domain.member.service;

import ssafy.ddada.api.member.response.MemberDetailResponse;
import ssafy.ddada.api.member.response.MemberSignupResponse;
import ssafy.ddada.domain.member.command.MemberSignupCommand;
import ssafy.ddada.domain.member.command.UpdateProfileCommand;

public interface MemberService {
    MemberSignupResponse signupMember(MemberSignupCommand signupCommand);
    MemberDetailResponse getMemberDetail();
    MemberDetailResponse updateMemberProfile(UpdateProfileCommand command);
    String deleteMember();
    Boolean checkNickname(String nickname);
//    MemberLoginResponse loginMember(MemberLoginCommand command);
}
