package nova.hackathon.util.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    // application.yml에 설정한 spring.mail.username 값 주입
    @Value("${spring.mail.username}")
    private String defaultFrom;

    public EmailDTO.Response send(EmailDTO.Request req) {
        MimeMessage mime = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mime, false, "UTF-8");
            helper.setFrom(defaultFrom);
            helper.setTo(req.getTo());
            helper.setSubject(req.getSubject());
            helper.setText(req.getContent(), req.isHtml());

            mailSender.send(mime);            // 실제 전송
            mime.saveChanges();               // Message-ID 생성 보장

            return EmailDTO.Response.builder()
                    .success(true)
                    .messageId(mime.getMessageID())
                    .build();

        } catch (MessagingException e) {
            return EmailDTO.Response.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }
}
