package ssafy.ddada.domain.member.player.service;

import ssafy.ddada.api.member.response.MemberDetailResponse;
import ssafy.ddada.api.member.response.MemberSignupResponse;
import ssafy.ddada.domain.member.player.command.MemberSignupCommand;
import ssafy.ddada.domain.member.player.command.UpdateProfileCommand;

public interface PlayerService {
    MemberSignupResponse signupMember(MemberSignupCommand signupCommand);
    MemberDetailResponse getMemberDetail();
    MemberDetailResponse updateMemberProfile(UpdateProfileCommand command);
    String deleteMember();
    Boolean checkNickname(String nickname);
//    MemberLoginResponse loginMember(MemberLoginCommand command);
}
