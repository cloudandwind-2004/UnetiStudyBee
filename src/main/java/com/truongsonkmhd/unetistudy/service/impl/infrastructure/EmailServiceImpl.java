package com.truongsonkmhd.unetistudy.service.impl.infrastructure;

import com.truongsonkmhd.unetistudy.service.infrastructure.EmailService;
import com.truongsonkmhd.unetistudy.utils.EmailTemplateUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Phiên bản gửi OTP trực tiếp - Bỏ qua RabbitMQ.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendOtpEmail(String to, String otp) {
        log.info("[EmailService] Đang gửi OTP cho {} trực tiếp qua SMTP...", to);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String htmlBody = EmailTemplateUtils.getOtpTemplate(otp);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Mã OTP xác thực - Uneti Study");
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            log.info("[EmailService] ✅ Đã gửi OTP thành công cho {}.", to);
            
        } catch (Exception e) {
            log.error("[EmailService] ❌ Lỗi khi gửi OTP cho {}: {}", to, e.getMessage());
        }
    }
}
