package ssafy.ddada.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record GmailStmlProperties (
        String host,
        int port,
        String username,
        String password,
        boolean auth,
        boolean starttlsEnable,
        boolean starttlsRequired,
        int connectionTimeout,
        int timeout,
        int writeTimeout,
        long authCodeExpirationMillis
    )
{}
