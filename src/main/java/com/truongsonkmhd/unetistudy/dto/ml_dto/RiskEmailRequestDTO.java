package com.truongsonkmhd.unetistudy.dto.ml_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEmailRequestDTO {
    private String riskLevel;
    private List<UUID> studentIds;
    private String subject;
    private String emailBody;
}
