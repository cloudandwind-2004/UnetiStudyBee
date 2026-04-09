package com.truongsonkmhd.unetistudy.dto.contest_lesson;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO hiển thị thông tin bảng điểm cho từng sinh viên trong bài thi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GradebookEntryDTO {
    UUID submissionId;
    UUID userId;
    String username;
    String fullName;
    String avatar;
    Double totalScore;
    Double quizScore;
    Double codingScore;
    Boolean isPassed;
    String status; // IN_PROGRESS, SUBMITTED, EXPIRED
    Instant startedAt;
    Instant submittedAt;
}
