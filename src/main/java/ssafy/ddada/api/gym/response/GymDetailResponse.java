package ssafy.ddada.api.gym.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Gym;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "체육관 정보 요약 응답 DTO")
public record GymDetailResponse(
        @Schema(description = "관리자 정보")
        GymAdminInfoResponse gymAdmin,
        @Schema(description = "경기 통계")
        List<MatchStatisticResponse> matchStatistics
) {
    public record GymAdminInfoResponse(
            Long id,
            String nickname,
            Integer income
    ) { }

    public record MatchStatisticResponse(
            LocalDate date,
            Integer matchCount
    ) { }

    // 해당 날짜에 매칭되는 경기 수 계산
    private static int getMatchCountForDate(Gym gym, LocalDate date) {
        return (int) gym.getCourts().stream()
                .flatMap(court -> court.getMatches().stream())
                .filter(match -> match.getMatchDate().isEqual(date))  // 해당 날짜와 일치하는 경기 필터링
                .count();
    }

    public static GymDetailResponse from(Gym gym) {
        GymAdminInfoResponse gymAdminInfo = new GymAdminInfoResponse(
                gym.getGymAdmin().getId(),
                gym.getGymAdmin().getNickname(),
                gym.getGymAdmin().getCumulativeIncome()
        );
        List<MatchStatisticResponse> matchStatistics = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);  // 일주일 전
        LocalDate endDate = today.plusDays(7);     // 일주일 후

        // 일주일 전부터 일주일 후까지 15일 동안의 통계 생성
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Integer matchCount = getMatchCountForDate(gym, date);
            matchStatistics.add(new MatchStatisticResponse(date, matchCount));
        }

        return new GymDetailResponse(
                gymAdminInfo,
                matchStatistics
        );
    }
}
