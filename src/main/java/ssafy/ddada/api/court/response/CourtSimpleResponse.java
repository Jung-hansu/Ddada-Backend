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
@Schema(description = "시설 정보 요약 응답 DTO")
public record CourtSimpleResponse(
        @Schema(description = "시설 ID")
        Long id,
        @Schema(description = "시설명")
        String name,
        @Schema(description = "시설 주소")
        String address,
        @Schema(description = "시설 사진")
        String image,
        @Schema(description = "시설 지역")
        String region,
        @Schema(description = "예약된 경기 시간 리스트")
        Map<LocalDate, List<LocalTime>> reservations
) {
    public static CourtSimpleResponse onMatchListFrom(Court court) {
        return new CourtSimpleResponse(
                court.getId(),
                court.getName(),
                court.getAddress(),
                null,
                court.getRegion().getKorValue(),
                null
        );
    }

    public static CourtSimpleResponse from(Court court) {
        return new CourtSimpleResponse(
                court.getId(),
                court.getName(),
                court.getAddress(),
                court.getImage(),
                court.getRegion().getKorValue(),
                court.getMatches()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Match::getMatchDate,
                                Collectors.mapping(Match::getMatchTime, Collectors.toList())
                        ))
        );
    }
}