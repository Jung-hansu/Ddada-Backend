package ssafy.ddada.domain.data.service;

import ssafy.ddada.api.data.response.PlayerAnalysticResponse;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;
import ssafy.ddada.api.data.response.RacketRecommendResponse;
import ssafy.ddada.domain.data.command.RacketRecommendCommand;

public interface DataService {
    PlayerMatchAnalyticsResponse PlayerMatchAnalytics(Long matchId);
    PlayerAnalysticResponse PlayerAnalytics();
    RacketRecommendResponse RecommendRacket(RacketRecommendCommand command);
}
