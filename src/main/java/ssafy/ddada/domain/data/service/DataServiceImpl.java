package ssafy.ddada.domain.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ssafy.ddada.api.data.response.PlayerAnalysticsResponse;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;
import ssafy.ddada.common.exception.data.DataNotFoundException;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.properties.WebClientProperties;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DataServiceImpl implements DataService {

    private final WebClient webClient;
    private final PlayerRepository playerRepository;
    private final WebClientProperties webClientProperties;

    private final String url = webClientProperties.url(); // 실제 API 엔드포인트로 변경 필요

    @Override
    public PlayerMatchAnalyticsResponse PlayerMatchAnalytics(Long matchId) {
        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);

        String requestUrl = url + playerId + "/" + matchId + "/";

        return webClient.get()
                .uri(requestUrl)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(errorBody -> Mono.error(new DataNotFoundException())
                                )
                )
                .bodyToMono(PlayerMatchAnalyticsResponse.class)
                .blockOptional()
                .orElseThrow(DataNotFoundException::new);
    }

    @Override
    public PlayerAnalysticsResponse PlayerAnalytics() {
        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);

        String requestUrl = url + playerId + "/";

        return webClient.get()
                .uri(requestUrl)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(errorBody -> Mono.error(new DataNotFoundException())
                                )
                )
                .bodyToMono(PlayerAnalysticsResponse.class)
                .blockOptional()
                .orElseThrow(DataNotFoundException::new);
    }
}
