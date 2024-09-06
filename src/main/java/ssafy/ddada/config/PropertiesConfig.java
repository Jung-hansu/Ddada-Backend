package ssafy.ddada.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ssafy.ddada.common.properties.CorsProperties;
import ssafy.ddada.common.properties.JwtProperties;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.common.properties.RedisProperties;

@Configuration
@EnableConfigurationProperties({
        CorsProperties.class,
        KakaoLoginProperties.class,
        JwtProperties.class,
        CoolSmsConfig.class,
        RedisProperties.class
})
public class PropertiesConfig {
}
