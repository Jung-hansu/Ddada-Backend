package ssafy.ddada.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ssafy.ddada.common.properties.GmailStmlProperties;
import java.util.Properties;

@Configuration
public class GmailConfig {

    private final GmailStmlProperties gmailSmtpProperties;

    public GmailConfig(GmailStmlProperties gmailSmtpProperties) {
        this.gmailSmtpProperties = gmailSmtpProperties;
    }

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
        properties.put("mail.smtp.auth", gmailSmtpProperties.auth());
        properties.put("mail.smtp.starttls.enable", gmailSmtpProperties.starttlsEnable());
        properties.put("mail.smtp.starttls.required", gmailSmtpProperties.starttlsRequired());
        properties.put("mail.smtp.connectiontimeout", gmailSmtpProperties.connectionTimeout());
        properties.put("mail.smtp.timeout", gmailSmtpProperties.timeout());
        properties.put("mail.smtp.writetimeout", gmailSmtpProperties.writeTimeout());

        return properties;
    }
}
