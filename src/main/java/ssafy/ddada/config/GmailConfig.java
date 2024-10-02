package ssafy.ddada.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ssafy.ddada.common.properties.GmailSmtpProperties;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class GmailConfig {

    private final GmailSmtpProperties gmailSmtpProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(gmailSmtpProperties.host());
        mailSender.setPort(gmailSmtpProperties.port());
        mailSender.setUsername(gmailSmtpProperties.username());
        mailSender.setPassword(gmailSmtpProperties.password());
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", gmailSmtpProperties.properties().mail().smtp().auth());
        properties.put("mail.smtp.starttls.enable", gmailSmtpProperties.properties().mail().smtp().starttls().enable());
        properties.put("mail.smtp.starttls.required", gmailSmtpProperties.properties().mail().smtp().starttls().required());
        properties.put("mail.smtp.connectiontimeout", gmailSmtpProperties.properties().mail().smtp().connectionTimeout());
        properties.put("mail.smtp.timeout", gmailSmtpProperties.properties().mail().smtp().timeout());
        properties.put("mail.smtp.writetimeout", gmailSmtpProperties.properties().mail().smtp().writeTimeout());

        return properties;
    }
}
