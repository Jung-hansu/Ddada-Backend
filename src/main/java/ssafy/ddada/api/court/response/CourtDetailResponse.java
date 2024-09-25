package ssafy.ddada.api.court.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.match.entity.Match;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "코트 검색 결과 DTO")
public record CourtDetailResponse(
        @Schema(description = "코트 ID")
        Long id,
        @Schema(description = "코트명")
        String name,
        @Schema(description = "체육관 주소")
        String address,
        @Schema(description = "체육관 전화번호")
        String contactNumber,
        @Schema(description = "체육관 설명")
        String description,
        @Schema(description = "체육관 이미지")
        String image,
        @Schema(description = "체육관 홈페이지 주소")
        String url,
        @Schema(description = "예약된 경기 시간 리스트")
        Map<LocalDate, List<LocalTime>> reservations
) {
    private static String getCourtName(Court court){
        return court.getGym().getName() + " " + court.getCourtNumber() + "번 코트";
    }

    public static CourtDetailResponse fromWhereMatchDetail(Court court){
        return new CourtDetailResponse(
                court.getId(),
                getCourtName(court),
                court.getGym().getAddress(),
                court.getGym().getContactNumber(),
                court.getGym().getDescription(),
                null,
                court.getGym().getUrl(),
                null
        );
    }

    public static CourtDetailResponse from(Court court){
        return new CourtDetailResponse(
                court.getId(),
                getCourtName(court),
                court.getGym().getAddress(),
                court.getGym().getContactNumber(),
                court.getGym().getDescription(),
                court.getGym().getImage(),
                court.getGym().getUrl(),
                court.getMatches()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Match::getMatchDate,
                                Collectors.mapping(Match::getMatchTime, Collectors.toList())
                        ))
        );
    }
}
