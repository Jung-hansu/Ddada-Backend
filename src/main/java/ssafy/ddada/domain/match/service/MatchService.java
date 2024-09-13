package ssafy.ddada.domain.match.service;

import org.springframework.data.domain.Page;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.domain.match.command.*;

public interface MatchService {

    Page<MatchSimpleResponse> getMatchesByKeyword(MatchSearchCommand command);
    void createMatch(Long creatorId, MatchCreateCommand command);
    MatchDetailResponse getMatchById(Long matchId);
    SetDetailResponse getSetById(Long matchId, Integer setNumber);
    void updateMatchStatus(MatchStatusChangeCommand command);

    // Team 관련 메소드
    @Deprecated TeamDetailResponse getTeamByTeamNumber(Long matchId, Integer teamNumber);
    void updateTeamPlayer(Long matchId, TeamChangePlayerCommand command);

    // Manager 관련 메소드
    Page<MatchSimpleResponse> getMatchesByManagerId(Long managerId, Integer page, Integer size);
    void allocateManager(Long matchId, Long managerId);
    void saveMatch(Long matchId, MatchResultCommand command);

}
