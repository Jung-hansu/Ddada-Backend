package ssafy.ddada.domain.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ssafy.ddada.domain.racket.entity.Racket;
import ssafy.ddada.domain.racket.repository.RacketRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static ssafy.ddada.common.util.ExcelUtil.isRowEmpty;

@Slf4j
//@Component
@RequiredArgsConstructor
public class RacketInitializer {

    private final RacketRepository racketRepository;

    @PostConstruct
    public void init() {
        initRacket();
    }

    private void initRacket() {
        log.info("[RacketInitializer] 라켓 초기화");
        final String filePath = "init/racket_data.xlsx";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            }

            List<Racket> rackets = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);  // 첫 번째 시트만 사용
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // 첫 줄은 헤더이므로 1부터 시작
                Row row = sheet.getRow(i);

                if (isRowEmpty(row)) {
                    break;
                }

                String name = row.getCell(2).getStringCellValue();
                Integer price = (int) row.getCell(3).getNumericCellValue();
                String balance = row.getCell(4).getStringCellValue();
                String weight = row.getCell(5).getStringCellValue();
                String shaft = row.getCell(6).getStringCellValue();
                String material = row.getCell(7).getStringCellValue();
                String color = row.getCell(8).getStringCellValue();
                String image = row.getCell(9).getStringCellValue();
                String manufacturer = row.getCell(10).getStringCellValue();

                Racket racket = Racket.builder()
                        .name(name)
                        .price(price)
                        .balance(balance)
                        .weight(weight)
                        .shaft(shaft)
                        .material(material)
                        .color(color)
                        .image(image)
                        .manufacturer(manufacturer)
                        .build();

                rackets.add(racket);
                log.debug("[RacketInitializer] 라켓 생성: {} / {}", i, sheet.getLastRowNum());
            }
            racketRepository.saveAll(rackets);
        } catch (IOException e){
            log.error("[RacketInitializer] 라켓 초기화 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}