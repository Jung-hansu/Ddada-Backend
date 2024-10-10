package ssafy.ddada.domain.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ssafy.ddada.api.data.response.PlayerAnalysticResponse;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;
import ssafy.ddada.api.data.response.RacketRecommendResponse;
import ssafy.ddada.common.exception.data.DataNotFoundException;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.properties.WebClientProperties;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.data.command.RacketRecommendCommand;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DataServiceImpl implements DataService {

    private final WebClient webClient;
    private final WebClientProperties webClientProperties;

    @Override
    public PlayerMatchAnalyticsResponse PlayerMatchAnalytics(Long matchId) {
        log.info("[DateService] 선수 경기 분석 >>>> 경기 ID: {}", matchId);
        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        String requestUrl = webClientProperties.url() + playerId + "/" + matchId + "/";

        return webClient.get()
                .uri(requestUrl)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(errorBody -> Mono.error(new DataNotFoundException()))
                )
                .bodyToMono(PlayerMatchAnalyticsResponse.class)
                .blockOptional()
                .orElseThrow(DataNotFoundException::new);
    }

    @Override
    public PlayerAnalysticResponse PlayerAnalytics() {
        log.info("[DataService] 선수 분석");
        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        String requestUrl = webClientProperties.url() + playerId + "/";

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
                .bodyToMono(PlayerAnalysticResponse.class)
                .blockOptional()
                .orElseThrow(DataNotFoundException::new);
    }

    @Override
    public RacketRecommendResponse RecommendRacket(RacketRecommendCommand command) {
        log.info("[DataService] 라켓 추천");
        String baseUrl = webClientProperties.url();

        // 경로 매개변수로 URL 생성
        String requestUrl = String.format("%s/rackets/%s/%s/%s/%s",
                baseUrl,
                command.balance(),
                command.weight(),
                command.price(),
                command.shaft()
        );

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(requestUrl);
        if (command.racketIds() != null && !command.racketIds().isEmpty()) {
            command.racketIds().forEach(racketId -> uriBuilder.queryParam("racket_id", racketId));
        }
        log.debug("[DataService] Request URL: {}", uriBuilder.toUriString());

        String finalUrl = uriBuilder.toUriString();

        // WebClient 요청
        ResponseEntity<String> response = webClient.get()
                .uri(finalUrl)
                .retrieve()
                .toEntity(String.class)
                .block();

        if (response.getStatusCode().is3xxRedirection()) {
            String redirectUrl = response.getHeaders().getLocation().toString();
            log.debug("[DataService] Redirect URL: {}", redirectUrl);

            response = webClient.get()
                    .uri(redirectUrl)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        }

        // 응답 본문 처리
        String responseString = response.getBody();
        log.debug("[DataService] Response Body: {}", responseString);

        try {
            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseString, RacketRecommendResponse.class);
        } catch (JsonProcessingException e) {
            log.error("JSON parsing error", e);
            throw new DataNotFoundException();
        }
    }
}