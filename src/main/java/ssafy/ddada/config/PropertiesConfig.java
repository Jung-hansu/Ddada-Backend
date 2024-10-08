package ssafy.ddada.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ssafy.ddada.common.properties.*;

@Configuration
@EnableConfigurationProperties({
        CorsProperties.class,
        RedisProperties.class,
        KakaoLoginProperties.class,
        JwtProperties.class,
        S3Properties.class,
        GmailSmtpProperties.class,
        CoolSmsProperties.class,
        ElasticsearchProperties.class,
        WebClientProperties.class
})
public class PropertiesConfig {
}
