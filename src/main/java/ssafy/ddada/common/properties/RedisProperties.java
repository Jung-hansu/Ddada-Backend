package ssafy.ddada.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties (
        String password,
        Cluster cluster
) {
    public record Cluster(
            Integer maxRedirects,
            Set<String> nodes
    ) { }
}
