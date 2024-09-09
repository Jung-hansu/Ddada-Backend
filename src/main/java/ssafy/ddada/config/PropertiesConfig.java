package ssafy.ddada.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ssafy.ddada.common.properties.CorsProperties;
import ssafy.ddada.common.properties.JwtProperties;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.common.properties.S3Properties;

@Configuration
@EnableConfigurationProperties({
        CorsProperties.class,
        KakaoLoginProperties.class,
        JwtProperties.class,
        S3Properties.class
})
public class PropertiesConfig {
}
