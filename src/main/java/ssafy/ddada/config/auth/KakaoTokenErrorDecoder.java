package ssafy.ddada.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssafy.ddada.api.auth.response.KakaoErrorResponse;
import ssafy.ddada.common.exception.KakaoTokenException;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class KakaoTokenErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() != null) {
            try {
                String message = Util.toString(response.body().asReader(Util.UTF_8));
                KakaoErrorResponse errorResponseForm =
                        objectMapper.readValue(message, KakaoErrorResponse.class);
                return new KakaoTokenException(
                        response.status(),
                        methodKey,
                        errorResponseForm.errorCode(),
                        errorResponseForm.errorDescription()
                );
            } catch (IOException e) {
                log.error(methodKey + "Error Deserializing response body from failed feign request response.", e);
            }
        }

        return new KakaoTokenException(response.status(), methodKey, "KAKAO_SERVER_ERROR", null);
    }
}
