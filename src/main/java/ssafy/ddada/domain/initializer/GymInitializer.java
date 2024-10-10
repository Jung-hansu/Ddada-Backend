package ssafy.ddada.domain.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.gym.entity.Gym;
import ssafy.ddada.domain.gym.entity.Region;
import ssafy.ddada.domain.gym.repository.GymRepository;
import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static ssafy.ddada.common.util.ExcelUtil.*;

@Slf4j
//@Component
//@DependsOn({"memberInitializer"})
@RequiredArgsConstructor
public class GymInitializer {

    private final GymRepository gymRepository;

    private static final List<GymAdmin> gymAdmins = new ArrayList<>();

    @PostConstruct
    public void init() {
        initGymAdmin();
        initGym();
    }

    // GymAdmin 데이터를 로드하여 DB에 저장하는 메서드
    private void initGymAdmin() {
        log.info("[GymInitializer] 체육관 관리자 초기화");
        String filePath = "init/gym_admin_data.xlsx";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            }

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);  // 첫 번째 시트만 사용
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // 첫 줄은 헤더이므로 1부터 시작
                Row row = sheet.getRow(i);

                if (isRowEmpty(row)) {
                    break;
                }

                String email = row.getCell(6).getStringCellValue();
                String nickname = row.getCell(7).getStringCellValue();
                String phoneNumber = row.getCell(8).getStringCellValue();
                String password = row.getCell(9).getStringCellValue();

                // GymAdmin 객체 생성
                GymAdmin gymAdmin = GymAdmin.builder()
                        .email(email)
                        .password(password)
                        .nickname(nickname)
                        .number(phoneNumber)
                        .build();

                // GymAdmin을 DB에 저장
                gymAdmins.add(gymAdmin);
                log.debug("[GymInitializer] 체육관 관리자 생성: {} / {}", i, sheet.getLastRowNum());
            }
        } catch (IOException e){
            log.error("[GymInitializer] 체육관 관리자 초기화 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    private void initGym() {
        log.info("[GymInitializer] 체육관 초기화");
        String filePath = "init/gym_data.xlsx";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            }

            List<Gym> gyms = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);  // 첫 번째 시트만 사용
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // 첫 줄은 헤더이므로 1부터 시작
                Row row = sheet.getRow(i);

                if (isRowEmpty(row)) {
                    break;
                }

                // gymAdminId로 GymAdmin 찾기
                GymAdmin gymAdmin = gymAdmins.get(i - 1);

                String name = row.getCell(8).getStringCellValue();
                String address = row.getCell(4).getStringCellValue();
                String contactNumber = row.getCell(5).getStringCellValue();
                String description = row.getCell(6).getStringCellValue();
                String image = row.getCell(7).getStringCellValue();
                String region = row.getCell(9).getStringCellValue();
                String url = row.getCell(10).getStringCellValue();

                // Gym 객체 생성 및 GymAdmin 연결
                Gym gym = Gym.builder()
                        .name(name)
                        .address(address)
                        .contactNumber(contactNumber)
                        .description(description)
                        .image(image)
                        .gymAdmin(gymAdmin)  // GymAdmin 연결
                        .region(Region.valueOf(region))
                        .url(url)
                        .build();

                // Court 3개 생성
                for (int j = 1; j <= 3; j++) {
                    Court court = Court.builder()
                            .gym(gym)
                            .courtNumber(j)  // 코트 번호 설정
                            .build();
                    gym.getCourts().add(court);
                }
                gyms.add(gym);  // Gym 저장
                log.debug("[GymInitializer] 체육관 생성: {} / {}", i, sheet.getLastRowNum());
            }
            gymRepository.saveAll(gyms);
        } catch (IOException e){
            log.error("[GymInitializer] 체육관 초기화 중 오류 발생: {}", e.getMessage(), e);
        }
    }

}
