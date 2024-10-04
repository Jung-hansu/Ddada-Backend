package ssafy.ddada.api.court.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Court;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "코트 정보 요약 응답 DTO")
public record CourtSimpleResponse(
        @Schema(description = "코트 ID")
        Long id,
        @Schema(description = "코트명")
        String name,
        @Schema(description = "체육관 주소")
        String address,
        @Schema(description = "체육관 사진")
        String image,
        @Schema(description = "체육관 지역")
        String region,
        @Schema(description = "예약된 경기 시간 리스트")
        Map<String, List<String>> reservations
) {
    private static String getCourtName(Court court){
        return court.getGym().getName() + " " + court.getCourtNumber() + "번 코트";
    }

    public static CourtSimpleResponse onMatchListFrom(Court court, String presignedUrl) {
        return new CourtSimpleResponse(
                court.getId(),
                getCourtName(court),
                court.getGym().getAddress(),
                presignedUrl,
                court.getGym().getRegion().getKorValue(),
                null
        );
    }

    public static CourtSimpleResponse from(Court court, String presignedUrl) {
        return new CourtSimpleResponse(
                court.getId(),
                court.getGym().getName(),
                court.getGym().getAddress(),
                presignedUrl,
                court.getGym().getRegion().getKorValue(),
                court.getMatches()
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        match -> match.getMatchDate().toString(),
                                        Collectors.mapping(
                                                match -> match.getMatchTime().toString(),
                                                Collectors.toList()
                                        )
                                )
                        )
        );
    }
}