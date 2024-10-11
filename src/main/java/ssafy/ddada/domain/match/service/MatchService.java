package ssafy.ddada.domain.match.service;

import org.springframework.data.domain.Page;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.domain.match.command.*;
import ssafy.ddada.domain.member.manager.command.ManagerSearchMatchCommand;

public interface MatchService {

    Page<MatchSimpleResponse> getFilteredMatches(MatchSearchCommand command);
    void createMatch(MatchCreateCommand command);
    MatchDetailResponse getMatchByIdWithInfos(Long matchId);
    void startMatch(Long matchId);
    boolean CheckPlayerBooked(CheckPlayerBookedCommand command);

    // Team 관련 메소드
    void setTeamPlayer(Long matchId, Integer teamNumber);
    void unsetTeamPlayer(Long matchId, Integer teamNumber);

    // Manager 관련 메소드
    Page<MatchSimpleResponse> getMatchesByManagerId(ManagerSearchMatchCommand command);
    void allocateManager(Long matchId);
    void deallocateManager(Long matchId);
    void saveMatch(Long matchId, MatchResultCommand command);
    void saveMatchAnalysisData(Long matchId);
}
