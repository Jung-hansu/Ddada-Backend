package ssafy.ddada.domain.data.service;

import ssafy.ddada.api.data.response.PlayerAnalysticsResponse;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;
import ssafy.ddada.api.data.response.RacketRecommendResponse;
import ssafy.ddada.domain.data.command.RacketRecommendCommand;

public interface DataService {
    PlayerMatchAnalyticsResponse PlayerMatchAnalytics(Long matchId);
    PlayerAnalysticsResponse PlayerAnalytics();
    RacketRecommendResponse ReccommandRacket(RacketRecommendCommand command);
}
