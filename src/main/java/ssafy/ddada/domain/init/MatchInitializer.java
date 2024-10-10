package ssafy.ddada.domain.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.common.exception.gym.CourtNotFoundException;
import ssafy.ddada.common.exception.manager.ManagerNotFoundException;
import ssafy.ddada.common.exception.match.MatchNotFoundException;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.repository.CourtRepository;
import ssafy.ddada.domain.match.entity.*;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.match.repository.ScoreRepository;
import ssafy.ddada.domain.match.repository.SetRepository;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ssafy.ddada.domain.init.ExcelUtil.*;
import static ssafy.ddada.common.util.ParameterUtil.*;

@Slf4j
//@Component
//@DependsOn({"memberInitializer", "gymInitializer"})
@RequiredArgsConstructor
public class MatchInitializer {

    private final MatchRepository matchRepository;
    private final CourtRepository courtRepository;
    private final PlayerRepository playerRepository;
    private final ManagerRepository managerRepository;
    private final SetRepository setRepository;
    private final ScoreRepository scoreRepository;

    private static List<Match> matches = new ArrayList<>();
    private static List<Set> sets = new ArrayList<>();

    @Transactional
    @PostConstruct
    public void init() {
        initMatch();
        initSet();
        initScore();
    }

    private void initMatch() {
        log.info("[MatchInitializer] 경기 초기화");
        String matchFilePath = "init/match_data.xlsx";
        String teamFilePath = "init/team_data.xlsx";
        try (InputStream matchInputStream = getClass().getClassLoader().getResourceAsStream(matchFilePath);
             InputStream teamInputStream = getClass().getClassLoader().getResourceAsStream(teamFilePath)) {
            if (matchInputStream == null || teamInputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + matchFilePath);
            }

            Workbook matchWorkbook = new XSSFWorkbook(matchInputStream);
            Workbook teamWorkbook = new XSSFWorkbook(teamInputStream);
            Sheet matchSheet = matchWorkbook.getSheetAt(0);
            Sheet teamSheet = teamWorkbook.getSheetAt(0);

            for (int i = 1, j = 1; i <= matchSheet.getLastRowNum(); i++, j += 2) {
                Row teamRow, matchRow = matchSheet.getRow(i);

                if (isRowEmpty(matchRow)) {
                    break;
                }

                Long courtId = parseLongIfNotNull(getCellValue(matchRow.getCell(0)));
                Long managerId = parseLongIfNotNull(getCellValue(matchRow.getCell(1)));
                Integer winnerTeamNumber = parseIntIfNotNull(getCellValue(matchRow.getCell(2)));
                Integer team1SetScore = parseIntIfNotNull(getCellValue(matchRow.getCell(3)));
                Integer team2SetScore = parseIntIfNotNull(getCellValue(matchRow.getCell(4)));
                MatchStatus status = MatchStatus.of(getCellValue(matchRow.getCell(5)));
                RankType rankType = RankType.of(getCellValue(matchRow.getCell(6)));
                MatchType matchType = MatchType.of(getCellValue(matchRow.getCell(7)));
                LocalDate matchDate = LocalDate.parse(getCellValue(matchRow.getCell(8)));
                LocalTime matchTime = LocalTime.parse(getCellValue(matchRow.getCell(9)));

                teamRow = teamSheet.getRow(j);
                Long team1Player1Id = parseLongIfNotNull(getCellValue(teamRow.getCell(0)));
                Long team1Player2Id = parseLongIfNotNull(getCellValue(teamRow.getCell(1)));
                int team1PlayerCount = Integer.parseInt(getCellValue(teamRow.getCell(2)));
                int team1Rating = Integer.parseInt(getCellValue(teamRow.getCell(3)));
                teamRow = teamSheet.getRow(j + 1);
                Long team2Player1Id = parseLongIfNotNull(getCellValue(teamRow.getCell(0)));
                Long team2Player2Id = parseLongIfNotNull(getCellValue(teamRow.getCell(1)));
                int team2PlayerCount = Integer.parseInt(getCellValue(teamRow.getCell(2)));
                int team2Rating = Integer.parseInt(getCellValue(teamRow.getCell(3)));

                Player team1Player1 = team1Player1Id != null ? playerRepository.findById(team1Player1Id).orElse(null) : null;
                Player team1Player2 = team1Player2Id != null ? playerRepository.findById(team1Player2Id).orElse(null) : null;
                Player team2Player1 = team2Player1Id != null ? playerRepository.findById(team2Player1Id).orElse(null) : null;
                Player team2Player2 = team2Player2Id != null ? playerRepository.findById(team2Player2Id).orElse(null) : null;

                Court court = courtRepository.findById(courtId).orElseThrow(CourtNotFoundException::new);
                Manager manager = managerId != null ? managerRepository.findById(managerId).orElseThrow(ManagerNotFoundException::new) : null;

                Match match = Match.builder()
                        .court(court)
                        .team1(Team.builder()
                                .player1(team1Player1)
                                .player2(team1Player2)
                                .playerCount(team1PlayerCount)
                                .rating(team1Rating)
                                .build()
                        )
                        .team2(Team.builder()
                                .player1(team2Player1)
                                .player2(team2Player2)
                                .playerCount(team2PlayerCount)
                                .rating(team2Rating)
                                .build()
                        )
                        .manager(manager)
                        .winnerTeamNumber(winnerTeamNumber)
                        .team1SetScore(team1SetScore)
                        .team2SetScore(team2SetScore)
                        .status(status)
                        .rankType(rankType)
                        .matchType(matchType)
                        .matchDate(matchDate)
                        .matchTime(matchTime)
                        .build();

                matches.add(match);
                log.debug("[MatchInitializer] 경기 생성률: {} / {}", i, matchSheet.getLastRowNum());
            }
            matches = matchRepository.saveAll(matches);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSet() {
        String filePath = "init/set_data.xlsx";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            }

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet setSheet = workbook.getSheetAt(0);

            for (int i = 1; i <= setSheet.getLastRowNum(); i++) {
                Row row = setSheet.getRow(i);

                if (isRowEmpty(row)) {
                    break;
                }

                Long matchId = Long.parseLong(getCellValue(row.getCell(0)));
                Integer setNumber = Integer.parseInt(getCellValue(row.getCell(1)));
                Integer setWinnerTeamNumber = Integer.parseInt(getCellValue(row.getCell(2)));
                Integer team1Score = Integer.parseInt(getCellValue(row.getCell(3)));
                Integer team2Score = Integer.parseInt(getCellValue(row.getCell(4)));

                Match match = matchRepository.findById(matchId).orElseThrow(MatchNotFoundException::new);

                Set set = Set.builder()
                        .match(match)
                        .setNumber(setNumber)
                        .setWinnerTeamNumber(setWinnerTeamNumber)
                        .team1Score(team1Score)
                        .team2Score(team2Score)
                        .build();

                sets.add(set);
                log.debug("[MatchInitializer] 세트 생성률: {} / {}", i, setSheet.getLastRowNum());
            }
            sets = setRepository.saveAll(sets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initScore(){
        String filePath = "init/score_data.xlsx";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            }

            List<Score> scores = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet scoreSheet = workbook.getSheetAt(0);

            for (int i = 1; i < scoreSheet.getLastRowNum(); i++) {
                Row row = scoreSheet.getRow(i);

                if (isRowEmpty(row)) {
                    break;
                }

                Long setId = Long.parseLong(getCellValue(row.getCell(0)));
                Integer earnedPlayer = parseIntIfNotNull(getCellValue(row.getCell(1)));
                Integer missedPlayer1 = parseIntIfNotNull(getCellValue(row.getCell(2)));
                Integer missedPlayer2 = parseIntIfNotNull(getCellValue(row.getCell(3)));
                Integer scoreNumber = Integer.parseInt(getCellValue(row.getCell(4)));
                EarnedType earnedType = EarnedType.parse(getCellValue(row.getCell(5)));
                MissedType missedType = MissedType.parse(getCellValue(row.getCell(6)));

                Set set = setRepository.findById(setId).orElseThrow(MatchNotFoundException::new);

                Score score = Score.builder()
                        .set(set)
                        .earnedPlayer(earnedPlayer)
                        .missedPlayer1(missedPlayer1)
                        .missedPlayer2(missedPlayer2)
                        .scoreNumber(scoreNumber)
                        .earnedType(earnedType)
                        .missedType(missedType)
                        .build();

                scores.add(score);
                log.debug("[MatchInitializer] 점수 생성률: {} / {}", i, scoreSheet.getLastRowNum());
            }
            scoreRepository.saveAll(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer parseIntIfNotNull(String num){
        return !isEmptyString(num) ? Integer.parseInt(num) : null;
    }

    private Long parseLongIfNotNull(String num){
        return !isEmptyString(num) ? Long.parseLong(num) : null;
    }

}
