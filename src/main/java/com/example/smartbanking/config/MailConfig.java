package com.example.smartbanking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Function;

@Configuration
public class MailConfig {

    // --- Standard Spring Mail properties (spring.mail.*) ---
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${spring.mail.protocol:smtp}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
    private boolean startTls;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout:5000}")
    private int connectionTimeoutMs;

    @Value("${spring.mail.properties.mail.smtp.timeout:5000}")
    private int readTimeoutMs;

    @Value("${spring.mail.properties.mail.smtp.writetimeout:5000}")
    private int writeTimeoutMs;

    // --- App-level convenience settings (optional) ---
    @Value("${app.mail.from.address:no-reply@smartbanking.local}")
    private String defaultFromAddress;

    @Value("${app.mail.from.name:SmartBanking}")
    private String defaultFromName;

    @Value("${app.mail.otp.subject:Your SmartBanking OTP}")
    private String otpSubject;

    /**
     * Primary JavaMailSender bean.
     * If you include `spring-boot-starter-mail`, Boot can auto-configure this,
     * but we define it explicitly so it also works in restricted environments.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setProtocol(protocol);

        // Optional auth (for providers that require it)
        if (!username.isBlank()) {
            sender.setUsername(username);
        }
        if (!password.isBlank()) {
            sender.setPassword(password);
        }

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", String.valueOf(smtpAuth));
        props.put("mail.smtp.starttls.enable", String.valueOf(startTls));
        props.put("mail.smtp.connectiontimeout", String.valueOf(connectionTimeoutMs));
        props.put("mail.smtp.timeout", String.valueOf(readTimeoutMs));
        props.put("mail.smtp.writetimeout", String.valueOf(writeTimeoutMs));

        // Recommended default encoding
        sender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return sender;
    }

    /**
     * A preconfigured simple mail template (plain text) you can clone and fill.
     * Useful for quick notifications or OTP emails without HTML.
     */
    @Bean
    public SimpleMailMessage simpleMailTemplate() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(formatFrom(defaultFromName, defaultFromAddress));
        return msg;
    }

    /**
     * Builder function to create a UTF-8 {@link MimeMessageHelper} quickly.
     * Usage:
     * <pre>
     * var mimeMessage = mailSender.createMimeMessage();
     * var helper = mimeMessageHelperFactory().apply(mimeMessage);
     * helper.setTo("user@example.com");
     * helper.setSubject("Subject");
     * helper.setText("<b>Hello</b>", true); // HTML
     * </pre>
     */
    @Bean
    public Function<jakarta.mail.internet.MimeMessage, MimeMessageHelper> mimeMessageHelperFactory() {
        return mimeMessage -> {
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                helper.setFrom(formatFrom(defaultFromName, defaultFromAddress));
                return helper;
            } catch (jakarta.mail.MessagingException e) {
                throw new IllegalStateException("Failed to create MimeMessageHelper", e);
            }
        };
    }

    /**
     * Convenience: build "Display Name <address>" if a name is provided.
     */
    private String formatFrom(String name, String address) {
        if (name == null || name.isBlank()) return address;
        return String.format("%s <%s>", name, address);
    }

    // --- Optional helpers for OTP flows ---

    /**
     * Provides the default subject for OTP emails.
     * Keep as a bean so services can @Autowired it.
     */
    @Bean
    public String otpMailSubject() {
        return otpSubject;
    }

    /**
     * Provides the default "from" address used by mail services.
     */
    @Bean
    public String defaultFromAddressBean() {
        return formatFrom(defaultFromName, defaultFromAddress);
    }
}