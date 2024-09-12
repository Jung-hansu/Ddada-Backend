package ssafy.ddada.api.court.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.match.entity.Match;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Schema(description = "시설 검색 결과 DTO")
public record CourtDetailResponse(
        @Schema(description = "시설 ID")
        Long id,
        @Schema(description = "시설명")
        String name,
        @Schema(description = "시설 주소")
        String address,
        @Schema(description = "시설 전화번호")
        String contactNumber,
        @Schema(description = "시설 설명")
        String description,
        @Schema(description = "시설 이미지")
        String image,
        @Schema(description = "시설 홈페이지 주소")
        String url,
        @Schema(description = "예약된 경기 시간 리스트")
        Map<LocalDate, List<LocalTime>> reservations
) {
    public static CourtDetailResponse from(Court court){
        return new CourtDetailResponse(
                court.getId(),
                court.getName(),
                court.getAddress(),
                court.getContactNumber(),
                court.getDescription(),
                court.getImage(),
                court.getUrl(),
                court.getMatches()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Match::getMatchDate,
                                Collectors.mapping(Match::getMatchTime, Collectors.toList())
                        ))
        );
    }
}
