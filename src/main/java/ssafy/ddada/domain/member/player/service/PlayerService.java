package ssafy.ddada.domain.member.player.service;

import ssafy.ddada.api.member.player.response.*;
import ssafy.ddada.domain.member.player.command.MemberSignupCommand;
import ssafy.ddada.domain.member.player.command.PasswordUpdateCommand;
import ssafy.ddada.domain.member.player.command.UpdateProfileCommand;

import java.util.List;

public interface PlayerService {
    PlayerSignupResponse signupMember(MemberSignupCommand signupCommand);
    PlayerProfileDetailResponse getMemberProfileDetail();
    PlayerDetailResponse getMemberDetail();
    PlayerDetailResponse updateMemberProfile(UpdateProfileCommand command);
    String deleteMember();
    boolean checkNickname(String nickname);
    String updateMemberPassword(PasswordUpdateCommand command);
    List<PlayerMatchResponse> getPlayerMatches();
    List<PlayerMatchResponse> getPlayerCompleteMatches();
    PlayerIdResponse getPlayerId();
    PlayerTotalMatchResponse getPlayerTotalMatch();
}
