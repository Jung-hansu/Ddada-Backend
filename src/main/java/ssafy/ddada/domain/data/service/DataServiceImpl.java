package ssafy.ddada.domain.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DataServiceImpl implements DataService {

    private final WebClient webClient;
    private final PlayerRepository playerRepository;

    String url = "http://j11a509.p.ssafy.io:8000/"; // 실제 API 엔드포인트로 변경 필요

    @Override
    public PlayerMatchAnalyticsResponse PlayerMatchAnalytics(Long matchId) {
        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(() -> new NotAuthenticatedException());

        String requestUrl = url + playerId + "/" + matchId + "/";

        try {
            // WebClient를 사용하여 GET 요청을 보내고 응답을 PlayerMatchAnalyticsResponse로 매핑
            PlayerMatchAnalyticsResponse response = webClient.get()
                    .uri(requestUrl)
                    .retrieve()
                    .bodyToMono(PlayerMatchAnalyticsResponse.class)
                    .block(); // 동기적으로 응답을 기다림

            if (response == null) {
                throw new RuntimeException("응답이 null입니다.");
            }
            log.info("PlayerMatchAnalyticsResponse: {}", response);
            return response;

        } catch (Exception e) {
            // 예외 처리 로직
            e.printStackTrace();
            throw new RuntimeException("PlayerMatchAnalytics 데이터를 가져오는 중 오류가 발생했습니다.", e);
        }
    }
}
