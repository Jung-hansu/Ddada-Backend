package ssafy.ddada.domain.data.service;

import ssafy.ddada.api.data.response.PlayerAnalysticsResponse;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;

public interface DataService {
    PlayerMatchAnalyticsResponse PlayerMatchAnalytics(Long matchId);
    PlayerAnalysticsResponse PlayerAnalytics();
}
