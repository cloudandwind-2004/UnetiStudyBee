package com.truongsonkmhd.unetistudy.service.impl.infrastructure;

import com.truongsonkmhd.unetistudy.service.infrastructure.BulkEmailService;
import com.truongsonkmhd.unetistudy.utils.EmailTemplateUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Phiên bản gửi mail trực tiếp (Synchronous/Async) - Bỏ qua RabbitMQ theo yêu cầu.
 * Sử dụng JavaMailSender để gửi trực tiếp.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BulkEmailServiceImpl implements BulkEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final int BATCH_SIZE = 50;

    @Override
    @Async // Chạy ngầm để không block UI, nhưng gửi trực tiếp không qua Queue
    public void sendBulkEmail(List<String> emails, String subject, String content) {
        if (emails == null || emails.isEmpty()) return;

        log.info("[BulkEmailService] ✉️ Bắt đầu gửi mail trực tiếp cho {} người...", emails.size());

        // Chia batch để dùng BCC hiệu quả
        List<List<String>> batches = partitionList(emails, BATCH_SIZE);

        for (List<String> batch : batches) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                String htmlBody = EmailTemplateUtils.getAiRiskAlertTemplate("Lớp học của bạn", content);

                helper.setFrom(fromEmail);
                helper.setSubject(subject);
                helper.setText(htmlBody, true);

                if (batch.size() == 1) {
                    helper.setTo(batch.get(0));
                } else {
                    helper.setTo(fromEmail); // Gửi cho chính mình
                    helper.setBcc(batch.toArray(new String[0]));
                }

                log.info("[BulkEmailService] 📤 Đang gửi batch {} người qua SMTP...", batch.size());
                mailSender.send(mimeMessage);
                
            } catch (Exception e) {
                log.error("[BulkEmailService] ❌ Lỗi khi gửi batch mail: {}", e.getMessage());
            }
        }

        log.info("[BulkEmailService] ✅ Hoàn thành tiến trình gửi mail.");
    }

    private <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}
