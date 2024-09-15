package ssafy.ddada.domain.match.service;

import org.springframework.data.domain.Page;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.domain.match.command.*;
import ssafy.ddada.domain.member.common.MemberRole;

public interface MatchService {

    Page<MatchSimpleResponse> getFilteredMatches(Long memberId, MemberRole role, MatchSearchCommand command);
    void createMatch(Long creatorId, MatchCreateCommand command);
    MatchDetailResponse getMatchById(Long matchId);
    SetDetailResponse getSetById(Long matchId, Integer setNumber);
    void updateMatchStatus(MatchStatusChangeCommand command);

    // Team 관련 메소드
    @Deprecated TeamDetailResponse getTeamByTeamNumber(Long matchId, Integer teamNumber);
    void setTeamPlayer(Long matchId, Long playerId, Integer teamNumber);
    void unsetTeamPlayer(Long matchId, Long playerId, Integer teamNumber);

    // Manager 관련 메소드
    Page<MatchSimpleResponse> getMatchesByManagerId(Long managerId, Integer page, Integer size);
    void allocateManager(Long matchId, Long managerId);
    void saveMatch(Long matchId, MatchResultCommand command);

}
