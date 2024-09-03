package ssafy.ddada.common.client.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenExpireResponse(
        Long id
) {
    public boolean success() {
        return id != null;
    }
}
