package ssafy.ddada.api.court.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Facility;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        @Schema(description = "예약된 경기 시간 리스트")
        Map<LocalDate, List<MatchTime>> reservations,
        @Schema(description = "시설 편의시설 목록")
        Set<Facility> facilities
) {
    public static CourtSimpleResponse from(Court court) {
        return new CourtSimpleResponse(
                court.getId(),
                court.getName(),
                court.getAddress(),
                court.getImage(),  // 그대로 이미지 경로를 사용
                court.getMatches()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Match::getMatchDate,
                                Collectors.mapping(Match::getMatchTime, Collectors.toList())
                        )),
                Facility.bitsToSet(court.getFacilities())
        );
    }

    public static CourtSimpleResponse from(Court court, String presignedUrl) {
        return new CourtSimpleResponse(
                court.getId(),
                court.getName(),
                court.getAddress(),
                presignedUrl,  // presigned URL을 사용
                court.getMatches()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Match::getMatchDate,
                                Collectors.mapping(Match::getMatchTime, Collectors.toList())
                        )),
                Facility.bitsToSet(court.getFacilities())
        );
    }
}