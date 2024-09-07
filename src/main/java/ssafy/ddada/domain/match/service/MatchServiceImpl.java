package ssafy.ddada.domain.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.match.response.MatchCreateResponse;
import ssafy.ddada.api.match.response.MatchDetailResponse;
import ssafy.ddada.api.match.response.MatchStatusChangeResponse;
import ssafy.ddada.common.exception.CourtNotFoundException;
import ssafy.ddada.common.exception.ManagerNotFoundException;
import ssafy.ddada.common.exception.MatchNotFoundException;
import ssafy.ddada.common.exception.TeamNotFoundException;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.repository.CourtRepository;
import ssafy.ddada.domain.match.command.MatchCreateCommand;
import ssafy.ddada.domain.match.command.MatchStatusChangeCommand;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.SearchedMatch;
import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.match.repository.TeamRepository;
import ssafy.ddada.domain.member.entity.Manager;
import ssafy.ddada.domain.member.repository.ManagerRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final CourtRepository courtRepository;
    private final ManagerRepository managerRepository;
    private final TeamRepository teamRepository;

    @Override
    public MatchDetailResponse getMatchById(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);
        return MatchDetailResponse.of(SearchedMatch.from(match));
    }

    @Override
    @Transactional
    public MatchStatusChangeResponse changeMatchStatus(MatchStatusChangeCommand command) {
        matchRepository.setMatchStatus(command.matchId(), command.status());
        return MatchStatusChangeResponse.of(command.status());
    }

//    TODO: Court, Team, Manager findById 작성 후 마저 작성
    @Override
    @Transactional
    public MatchCreateResponse createMatch(MatchCreateCommand command) {
        Court court = courtRepository.findById(command.court_id())
                .orElseThrow(CourtNotFoundException::new);
        Manager manager = managerRepository.findById(command.manager_id())
                .orElseThrow(ManagerNotFoundException::new);
        Team team1 = teamRepository.findById(command.team1_id())
                .orElseThrow(TeamNotFoundException::new);
        Team team2 = teamRepository.findById(command.team2_id())
                .orElseThrow(TeamNotFoundException::new);
        Match match = Match.createMatch(court, team1, team2, manager, command.isSingle(), command.matchDate());
        matchRepository.save(match);
        return MatchCreateResponse.of();
    }


}
