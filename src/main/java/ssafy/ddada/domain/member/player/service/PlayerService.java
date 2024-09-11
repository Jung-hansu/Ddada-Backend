package ssafy.ddada.domain.member.player.service;

import ssafy.ddada.api.member.player.response.PlayerDetailResponse;
import ssafy.ddada.api.member.player.response.PlayerSignupResponse;
import ssafy.ddada.domain.member.player.command.MemberSignupCommand;
import ssafy.ddada.domain.member.player.command.UpdateProfileCommand;

public interface PlayerService {
    PlayerSignupResponse signupMember(MemberSignupCommand signupCommand);
    PlayerDetailResponse getMemberDetail();
    PlayerDetailResponse updateMemberProfile(UpdateProfileCommand command);
    String deleteMember();
    Boolean checkNickname(String nickname);
//    PlayerLoginResponse loginMember(MemberLoginCommand command);
}
