package ssafy.ddada.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record GmailSmtpProperties(
        String host,
        int port,
        String username,
        String password,
        Properties properties,
        long authCodeExpirationMillis
) {
    public record Properties(
            Mail mail
    ) {
        public record Mail(
                Smtp smtp
        ) {
            public record Smtp(
                    boolean auth,
                    Starttls starttls,
                    int connectionTimeout,
                    int timeout,
                    int writeTimeout
            ) {
                public record Starttls(
                        boolean enable,
                        boolean required
                ) {}
            }
        }
    }
}