package ssafy.ddada.domain.match.service;

import ssafy.ddada.api.match.response.MatchCreateResponse;
import ssafy.ddada.api.match.response.MatchDetailResponse;
import ssafy.ddada.api.match.response.MatchStatusChangeResponse;
import ssafy.ddada.domain.match.command.MatchCreateCommand;
import ssafy.ddada.domain.match.command.MatchStatusChangeCommand;

public interface MatchService {

    MatchDetailResponse getMatchById(Long matchId);
    MatchStatusChangeResponse changeMatchStatus(MatchStatusChangeCommand command);
    MatchCreateResponse createMatch(MatchCreateCommand command);
}
