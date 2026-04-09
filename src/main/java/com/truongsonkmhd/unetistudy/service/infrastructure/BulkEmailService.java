package com.truongsonkmhd.unetistudy.service.infrastructure;

import java.util.List;

/**
 * [ISP] Interface chỉ chịu trách nhiệm gửi email hàng loạt (thông báo cảnh báo, đánh giá).
 * Tách riêng khỏi EmailService vì bulk email có chiến lược xử lý khác
 * (batch, retry, async) so với email đơn lẻ.
 */
public interface BulkEmailService {
    void sendBulkEmail(List<String> emails, String subject, String content);
}
