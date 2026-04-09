package com.truongsonkmhd.unetistudy.dto.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailMessageDTO implements Serializable {
    private List<String> to;
    private String subject;
    private String content;
    private String mailType; // e.g., "AI_RISK_ALERT", "OTP"
    private boolean isHtml;
}
