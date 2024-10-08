package ssafy.ddada.api.gym.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Gym;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "체육관 정보 요약 응답 DTO")
public record GymDetailResponse(
        @Schema(description = "관리자 정보")
        GymAdminInfo gymAdmin,

        @Schema(description = "경기 통계")
        List<MatchStatistic> matchStatistics
) {
    public record GymAdminInfo(
            Long id,
            String nickname,
            Integer income
    ) { }

    public record MatchStatistic(
            LocalDate date,
            Integer matchCount
    ) { }

    private static int getMatchCountForDate(Gym gym, LocalDate date) {
        return (int) gym.getCourts().stream()
                .flatMap(court -> court.getMatches().stream())
                .filter(match -> match.getMatchDate().isEqual(date))  // 해당 날짜와 일치하는 경기 필터링
                .count();
    }

    public static GymDetailResponse from(Gym gym) {
        GymAdminInfo gymAdminInfo = new GymAdminInfo(
                gym.getGymAdmin().getId(),
                gym.getGymAdmin().getNickname(),
                gym.getGymAdmin().getCumulativeIncome()
        );
        List<MatchStatistic> matchStatistics = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.plusDays(7);

        // 일주일 전부터 일주일 후까지 15일 동안의 통계 생성
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Integer matchCount = getMatchCountForDate(gym, date);
            matchStatistics.add(new MatchStatistic(date, matchCount));
        }

        return new GymDetailResponse(gymAdminInfo, matchStatistics);
    }
}
