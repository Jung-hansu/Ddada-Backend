package ssafy.ddada.config;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ssafy.ddada.common.properties.CoolSmsProperties;

@Configuration
@RequiredArgsConstructor
public class CoolSmsConfig {
    private final CoolSmsProperties coolSmsProperties;

    @Bean
    public DefaultMessageService coolSmsService() {
        return NurigoApp.INSTANCE.initialize(coolSmsProperties.apiKey(), coolSmsProperties.apiSecret(), "https://api.coolsms.co.kr");
    }
}
