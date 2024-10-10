package ssafy.ddada.domain.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.member.common.Gender;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ssafy.ddada.common.util.ExcelUtil.*;

@Slf4j
//@Component
@RequiredArgsConstructor
public class MemberInitializer {

    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    private final ManagerRepository managerRepository;

    @PostConstruct
    public void init() {
        initManager();
        initPlayer();
    }

    private void initPlayer() {
        log.info("[MemberInitializer] 선수 초기화");
        final String filePath = "init/player_data.xlsx";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            }

            List<Player> players = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (isRowEmpty(row)) {
                    break;
                }

                String email = getCellValue(row.getCell(0));
                String password = getCellValue(row.getCell(1));
                String nickname = getCellValue(row.getCell(2));
                String image = getCellValue(row.getCell(3));
                String number = getCellValue(row.getCell(4));
                String gender = getCellValue(row.getCell(5));
                String birth = getCellValue(row.getCell(6));
                String description = getCellValue(row.getCell(7));
                int rating = Integer.parseInt(getCellValue(row.getCell(8)));
                int winStreak = Integer.parseInt(getCellValue(row.getCell(9)));
                int loseStreak = Integer.parseInt(getCellValue(row.getCell(10)));
                int gameCount = Integer.parseInt(getCellValue(row.getCell(11)));

                Player player = Player.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .nickname(nickname)
                        .image(image)
                        .number(number)
                        .gender(Gender.parse(gender.toUpperCase()))
                        .birth(LocalDate.parse(birth))
                        .description(description)
                        .rating(rating)
                        .winStreak(winStreak)
                        .loseStreak(loseStreak)
                        .gameCount(gameCount)
                        .build();

                log.debug("[MemberInitializer] 플레이어 생성: {} / {}", i, sheet.getLastRowNum());
                players.add(player);
            }
            playerRepository.saveAll(players);
        } catch (IOException e) {
            log.error("[MemberInitializer] 선수 초기화 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    private void initManager() {
        log.info("[MemberInitializer] 매니저 초기화");
        final String filePath = "init/manager_data.xlsx";
        try (InputStream managerInputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (managerInputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            }

            List<Manager> managers = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(managerInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (isRowEmpty(row)) {
                    break;
                }

                String email = getCellValue(row.getCell(0));
                String password = getCellValue(row.getCell(1));
                String nickname = getCellValue(row.getCell(2));
                String number = getCellValue(row.getCell(3));

                Manager manager = Manager.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .nickname(nickname)
                        .number(number)
                        .description("안녕하세요")
                        .build();

                log.debug("[MemberInitializer] 매니저 생성 : {} / {}", i, sheet.getLastRowNum());
                managers.add(manager);
            }
            managerRepository.saveAll(managers);
        } catch (IOException e) {
            log.error("[MemberInitializer] 매니저 초기화 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}