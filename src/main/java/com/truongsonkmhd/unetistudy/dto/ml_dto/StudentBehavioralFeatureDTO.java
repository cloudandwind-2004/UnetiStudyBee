package com.truongsonkmhd.unetistudy.dto.ml_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentBehavioralFeatureDTO {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("course_id")
    private String courseId;

    @JsonProperty("completion_rate")
    private Double completionRate;       // % bài DONE / tổng bài

    @JsonProperty("avg_watched_percent")
    private Double avgWatchedPercent;    // tb % video đã xem

    @JsonProperty("time_spent_total")
    private Integer timeSpentTotal;      // tổng giây học

    @JsonProperty("active_days_7")
    private Integer activeDays7;         // số ngày học trong 7 ngày gần nhất

    @JsonProperty("last_access_gap_days")
    private Integer lastAccessGapDays;   // số ngày không học

    @JsonProperty("quiz_fail_rate")
    private Double quizFailRate;         // tỉ lệ lần quiz không pass (cần cho RISK cluster)

    /**
     * progress_efficiency = completionRate / expectedProgressRate
     * Ý nghĩa: sinh viên hoàn thành được bao nhiêu % so với KỲ VỌNG tại thời điểm hiện tại.
     * - > 1.0 : vượt tiến độ (chăm chỉ / nhanh)
     * - ~ 1.0 : đúng tiến độ
     * - < 1.0 : chậm tiến độ
     * - Capped về [0.0, 2.0] để tránh outlier ảnh hưởng K-Means
     */
    @JsonProperty("progress_efficiency")
    private Double progressEfficiency;
}
