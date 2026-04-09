package com.truongsonkmhd.unetistudy.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helper class để generate HTML email chuyên nghiệp.
 */
public class EmailTemplateUtils {

    public static String getAiRiskAlertTemplate(String className, String content) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    .container { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; }
                    .header { background-color: #d32f2f; color: white; padding: 20px; text-align: center; }
                    .content { padding: 30px; line-height: 1.6; color: #333; }
                    .footer { background-color: #f5f5f5; padding: 15px; text-align: center; font-size: 12px; color: #777; }
                    .button { background-color: #d32f2f; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; display: inline-block; margin-top: 20px; }
                    .highlight { color: #d32f2f; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>CẢNH BÁO TÌNH TRẠNG HỌC TẬP</h2>
                    </div>
                    <div class="content">
                        <p>Kính gửi bạn học sinh,</p>
                        <p>Hệ thống AI của chúng tôi đã phân tích và nhận thấy bạn đang gặp một số thử thách trong lớp học: <br>
                           <span class="highlight">[%s]</span></p>
                        <div style="background-color: #fff4f4; padding: 15px; border-left: 4px solid #d32f2f; margin: 20px 0;">
                            %s
                        </div>
                        <p>Chúng tôi rất quan tâm đến kết quả học tập của bạn. Để cải thiện tình hình, bạn nên:</p>
                        <ul>
                            <li>Xem lại các video bài giảng gần đây.</li>
                            <li>Hoàn thành các bài tập còn tồn đọng.</li>
                            <li>Liên hệ trực tiếp với giáo viên nếu cần hỗ trợ.</li>
                        </ul>
                        <p>Chúc bạn học tập tiến bộ!</p>
                        <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
                        <p style="font-style: italic;">Đội ngũ giảng dạy — Uneti Study AI</p>
                    </div>
                    <div class="footer">
                        Email này được gửi tự động từ hệ thống Uneti Study vào lúc %s
                    </div>
                </div>
            </body>
            </html>
            """.formatted(className, content, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
    }
    
    public static String getOtpTemplate(String otp) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    .container { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 500px; margin: auto; border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden; }
                    .header { background-color: #1a73e8; color: white; padding: 20px; text-align: center; }
                    .content { padding: 30px; line-height: 1.6; color: #333; }
                    .footer { background-color: #f5f5f5; padding: 15px; text-align: center; font-size: 12px; color: #777; }
                    .otp-box { background-color: #f1f3f4; text-align: center; padding: 15px; font-size: 28px; font-weight: bold; letter-spacing: 5px; color: #d93025; border-radius: 5px; margin: 20px 0; border: 1px dashed #d93025; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>XÁC THỰC TÀI KHOẢN</h2>
                    </div>
                    <div class="content">
                        <p>Chào bạn,</p>
                        <p>Bạn đã yêu cầu mã OTP để khôi phục mật khẩu hoặc xác thực tài khoản tại <b>Uneti Study</b>.</p>
                        <p>Mã OTP của bạn là:</p>
                        <div class="otp-box">
                            %s
                        </div>
                        <p style="font-size: 13px; color: #5f6368;">Mã này có hiệu lực trong 2 phút. Vui lòng không cung cấp mã này cho bất kỳ ai.</p>
                        <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>
                        <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
                        <p style="font-style: italic;">Trân trọng,<br>Đội ngũ Uneti Study</p>
                    </div>
                    <div class="footer">
                        &copy; 2024 Uneti Study Team. All rights reserved.
                    </div>
                </div>
            </body>
            </html>
            """.formatted(otp);
    }
}
