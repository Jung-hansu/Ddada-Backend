package ssafy.ddada.domain.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientResponse;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DataServiceImpl implements DataService {

    private final WebClient webClient;
    private final WebClientProperties webClientProperties;

    @Override
    public PlayerMatchAnalyticsResponse PlayerMatchAnalytics(Long matchId) {
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
                                .flatMap(errorBody -> Mono.error(new DataNotFoundException())
                                )
                )
                .bodyToMono(PlayerMatchAnalyticsResponse.class)
                .blockOptional()
                .orElseThrow(DataNotFoundException::new);
    }

    @Override
    public PlayerAnalysticResponse PlayerAnalytics() {
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
        // 기본 URL
        String baseUrl = webClientProperties.url();

        // 경로 매개변수로 URL 생성
        String requestUrl = String.format("%s/rackets/%s/%s/%s/%s",
                baseUrl,
                command.balance(),
                command.weight(),
                command.price(),
                command.shaft()
        );

        // 쿼리 파라미터 추가
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(requestUrl);
        if (command.racketIds() != null && !command.racketIds().isEmpty()) {
            command.racketIds().forEach(racket -> uriBuilder.queryParam("racket_id", racket.byteValue()));
        }
        log.info("Request URL: {}", uriBuilder.toUriString());

        String finalUrl = uriBuilder.toUriString();

        // WebClient 요청
        ClientResponse response = webClient.get()
                .uri(finalUrl)
                .exchange()
                .block();

        if (response.statusCode().is3xxRedirection()) {
            // 리다이렉션 처리
            String redirectUrl = response.headers().header("Location").get(0);  // Location 헤더에서 리다이렉션 URL 가져오기
            log.info("Redirecting to: {}", redirectUrl);
            response = webClient.get()
                    .uri(redirectUrl)
                    .exchange()
                    .block();  // 리다이렉션된 URL로 재요청
        }

        String responseString = response.bodyToMono(String.class).block();
        log.info("Response Body: {}", responseString);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseString, RacketRecommendResponse.class);
        } catch (JsonProcessingException e) {
            log.error("JSON parsing error", e);
            throw new DataNotFoundException();  // 필요한 예외 던짐
        }
    }

}