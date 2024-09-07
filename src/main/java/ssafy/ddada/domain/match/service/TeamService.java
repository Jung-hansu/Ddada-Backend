package ssafy.ddada.domain.match.service;

import ssafy.ddada.api.match.response.TeamCreateResponse;
import ssafy.ddada.api.match.response.TeamDetailResponse;
import ssafy.ddada.domain.match.command.TeamCreateCommand;

public interface TeamService {

    TeamCreateResponse createTeam(TeamCreateCommand command);
    TeamDetailResponse getTeamById(Long teamId);

}
