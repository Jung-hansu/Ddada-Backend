package ssafy.ddada.domain.match.service;

import org.springframework.data.domain.Page;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.domain.match.command.*;
import ssafy.ddada.domain.member.manager.command.ManagerSearchMatchCommand;

public interface MatchService {

    Page<MatchSimpleResponse> getFilteredMatches(Long memberId, MatchSearchCommand command);
    void createMatch(Long creatorId, MatchCreateCommand command);
    MatchDetailResponse getMatchByIdWithInfos(Long matchId);
    SetDetailResponse getSetsByIdWithInfos(Long matchId, Integer setNumber);
    void updateMatchStatus(MatchStatusChangeCommand command);

    // Team 관련 메소드
    void setTeamPlayer(Long matchId, Long playerId, Integer teamNumber);
    void unsetTeamPlayer(Long matchId, Long playerId, Integer teamNumber);

    // Manager 관련 메소드
    Page<MatchSimpleResponse> getMatchesByManagerId(ManagerSearchMatchCommand command);
    void allocateManager(Long matchId, Long managerId);
    void saveMatch(Long matchId, Long managerId, MatchResultCommand command);

}
