package ssafy.ddada.api.gym.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.gym.entity.Gym;
import ssafy.ddada.domain.match.entity.Match;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "체육관 정보 요약 응답 DTO")
public record GymSimpleResponse(
        @Schema(description = "체육관 ID")
        Long id,
        @Schema(description = "체육관명")
        String name,
        @Schema(description = "체육관 주소")
        String address,
        @Schema(description = "체육관 사진")
        String image,
        @Schema(description = "체육관 지역")
        String region,
        @Schema(description = "예약된 경기 시간 리스트")
        Map<LocalDate, List<LocalTime>> reservations
) {
    public static GymSimpleResponse onMatchListFrom(Gym gym) {
        return new GymSimpleResponse(
                gym.getId(),
                gym.getName(),
                gym.getAddress(),
                null,
                gym.getRegion().getKorValue(),
                null
        );
    }

    public static GymSimpleResponse from(Gym gym) {
        return new GymSimpleResponse(
                gym.getId(),
                gym.getName(),
                gym.getAddress(),
                gym.getImage(),
                gym.getRegion().getKorValue(),
                gym.getMatches()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Match::getMatchDate,
                                Collectors.mapping(Match::getMatchTime, Collectors.toList())
                        ))
        );
    }
}