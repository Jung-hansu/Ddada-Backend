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
@Schema(description = "체육관 검색 결과 DTO")
public record GymDetailResponse(
        @Schema(description = "체육관 ID")
        Long id,
        @Schema(description = "체육관명")
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
    public static GymDetailResponse fromWhereMatchDetail(Gym gym){
        return new GymDetailResponse(
                gym.getId(),
                gym.getName(),
                gym.getAddress(),
                gym.getContactNumber(),
                gym.getDescription(),
                null,
                gym.getUrl(),
                null
        );
    }

    public static GymDetailResponse from(Gym gym){
        return new GymDetailResponse(
                gym.getId(),
                gym.getName(),
                gym.getAddress(),
                gym.getContactNumber(),
                gym.getDescription(),
                gym.getImage(),
                gym.getUrl(),
                gym.getMatches()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Match::getMatchDate,
                                Collectors.mapping(Match::getMatchTime, Collectors.toList())
                        ))
        );
    }
}
