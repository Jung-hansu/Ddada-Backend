package ssafy.ddada.domain.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.match.response.TeamCreateResponse;
import ssafy.ddada.api.match.response.TeamDetailResponse;
import ssafy.ddada.common.exception.NotFoundMemberException;
import ssafy.ddada.common.exception.TeamNotFoundException;
import ssafy.ddada.domain.match.command.TeamCreateCommand;
import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.domain.match.repository.TeamRepository;
import ssafy.ddada.domain.member.entity.Member;
import ssafy.ddada.domain.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @Override
    public TeamCreateResponse createTeam(TeamCreateCommand command){
        Member player1 = memberRepository.findById(command.player1_id())
                        .orElseThrow(NotFoundMemberException::new);
        Member player2 = memberRepository.findById(command.player2_id())
                        .orElseThrow(NotFoundMemberException::new);

        Team team = Team.createTeam(player1, player2);
        Long teamId = teamRepository.save(team).getId();
        return TeamCreateResponse.of(teamId);
    }

    @Override
    public TeamDetailResponse getTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);
        return TeamDetailResponse.of(teamId, team.getPlayer1(), team.getPlayer2());
    }

}
