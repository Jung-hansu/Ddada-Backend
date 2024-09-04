package ssafy.ddada.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ssafy.ddada.common.properties.CorsProperties;
import ssafy.ddada.common.properties.JwtProperties;
import ssafy.ddada.common.properties.KakaoLoginProperties;

@Configuration
@EnableConfigurationProperties({
        CorsProperties.class,
        KakaoLoginProperties.class,
        JwtProperties.class,
        CoolSmsConfig.class
})
public class PropertiesConfig {
}
