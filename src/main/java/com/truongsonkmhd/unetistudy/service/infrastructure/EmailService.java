package com.truongsonkmhd.unetistudy.service.infrastructure;

/**
 * [ISP] Interface chỉ chịu trách nhiệm gửi email đơn lẻ (OTP, thông báo cá nhân).
 */
public interface EmailService {
    void sendOtpEmail(String to, String otp);
}
