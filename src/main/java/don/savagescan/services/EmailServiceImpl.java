package don.savagescan.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {
    private final JavaMailSender emailSender;

    @Value("${setTo}")
    private String setTo;

    @Value("${spring.mail.username}")
    private String setFrom;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMail(String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(setFrom);
        message.setTo(setTo);
        message.setSubject("New SSH found");
        message.setText(text);

        emailSender.send(message);

    }
}
