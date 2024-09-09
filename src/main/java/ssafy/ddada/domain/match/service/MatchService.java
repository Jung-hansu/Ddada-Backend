package ssafy.ddada.domain.match.service;

import org.springframework.data.domain.Page;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.domain.match.command.MatchCreateCommand;
import ssafy.ddada.domain.match.command.MatchResultCommand;
import ssafy.ddada.domain.match.command.MatchStatusChangeCommand;
import ssafy.ddada.domain.match.command.TeamChangePlayerCommand;

public interface MatchService {

    Page<MatchSimpleResponse> getMatchesByKeyword(String keyword, Integer page, Integer size);
    MatchDetailResponse createMatch(Long creatorId, MatchCreateCommand command);
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
