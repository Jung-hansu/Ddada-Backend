package ssafy.ddada.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ssafy.ddada.common.properties.CorsProperties;

@Configuration
@EnableConfigurationProperties({
        CorsProperties.class
})
public class PropertiesConfig {
}
