package com.example.smartbanking.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EmailUtil {

    private final JavaMailSender mailSender;
    private final Function<MimeMessage, MimeMessageHelper> helperFactory;
    private final String fromAddress;     // injected bean
    private final String otpSubject;      // injected bean

    public EmailUtil(JavaMailSender mailSender,
                     Function<MimeMessage, MimeMessageHelper> mimeMessageHelperFactory,
                     @Qualifier("defaultFromAddressBean") String fromAddress,
                     @Qualifier("otpMailSubject") String otpSubject) {
        this.mailSender = mailSender;
        this.helperFactory = mimeMessageHelperFactory;
        this.fromAddress = fromAddress;
        this.otpSubject = otpSubject;
    }

    public void sendText(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAddress);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    public void sendHtml(String to, String subject, String html) throws MessagingException {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = helperFactory.apply(mime);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(mime);
    }

    public void sendWithAttachment(String to, String subject, String html,
                                   String filename, byte[] content, String contentType) throws MessagingException {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = helperFactory.apply(mime);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        helper.addAttachment(filename, new ByteArrayResource(content), contentType);
        mailSender.send(mime);
    }

    public void sendOtp(String to, String code) throws MessagingException {
        String html = "<p>Your OTP is <b>" + code + "</b>. It expires in 5 minutes.</p>";
        sendHtml(to, otpSubject, html);
    }
}
