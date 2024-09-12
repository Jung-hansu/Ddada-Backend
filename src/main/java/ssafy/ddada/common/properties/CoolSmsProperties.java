package ssafy.ddada.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "coolsms")
public record CoolSmsProperties (
    String apiKey,
    String apiSecret,
    String fromNumber
){
}
