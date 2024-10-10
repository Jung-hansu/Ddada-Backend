package ssafy.ddada.domain.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

import static ssafy.ddada.domain.init.ExcelUtil.*;

@Slf4j
//@Component
//@DependsOn({"memberInitializer"})
@RequiredArgsConstructor
public class GymInitializer {

    private final GymRepository gymRepository;

    @Transactional
    @PostConstruct
    public void init() {

        String gymAdminFilePath = "init/gym_admin_data.xlsx";
        String gymFilePath = "init/gym_data.xlsx";

        try {
            // GymAdmin 데이터 읽기 및 저장
            loadAndSaveGymAdminData(gymAdminFilePath, gymFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data file", e);
        }
    }

    // GymAdmin 데이터를 로드하여 DB에 저장하는 메서드
    private void loadAndSaveGymAdminData(String filePath, String gymFilePath) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            }

            List<GymAdmin> gymAdmins = new ArrayList<>();
            List<Gym> gyms = new ArrayList<>();
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
                log.debug("[GymInitializer] 체육관 관리자 생성: {}", gymAdmin);
            }
            try (InputStream gymInputStream = getClass().getClassLoader().getResourceAsStream(gymFilePath)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + gymFilePath);
                }

                Workbook gymWorkbook = new XSSFWorkbook(gymInputStream);
                Sheet gymSheet = gymWorkbook.getSheetAt(0);  // 첫 번째 시트만 사용
                for (int i = 1; i <= gymSheet.getLastRowNum(); i++) {  // 첫 줄은 헤더이므로 1부터 시작
                    Row row = gymSheet.getRow(i);

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
                }
            }
            gymRepository.saveAll(gyms);
        }
    }
}
