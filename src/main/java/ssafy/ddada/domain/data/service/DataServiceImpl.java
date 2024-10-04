package ssafy.ddada.domain.data.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DataServiceImpl {
    private final WebClient webClient;


    public String getFastApiData() {
        // FastAPI 서버에 GET 요청 보내기
        Mono<String> response = webClient.get()
                .uri("http://localhost:8000/data")  // FastAPI 서버 주소
                .retrieve()
                .bodyToMono(String.class);

        // 응답을 차단하여 동기식으로 변환 후 반환
        return response.block();
    }

    public String sendItemToFastApi(String name, String description, double price) {
        // FastAPI로 보낼 데이터
        ItemRequest itemRequest = new ItemRequest(name, description, price);

        Mono<String> response = webClient.post()
                .uri("http://localhost:8000/item/")  // FastAPI 서버의 POST 엔드포인트
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(String.class);

        return response.block();
    }

    // 내부적으로 사용할 요청 모델
    static class ItemRequest {
        private String name;
        private String description;
        private double price;

        public ItemRequest(String name, String description, double price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }

        // getter와 setter 생략
    }

}
