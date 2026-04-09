package com.truongsonkmhd.unetistudy.dto.contest_lesson;

import com.truongsonkmhd.unetistudy.common.ContestType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * DTO trả về bảng điểm tổng hợp cho một bài thi cụ thể trong một lớp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GradebookResponse {
    // Thông tin bài thi
    UUID classContestId;
    String contestTitle;
    String contestDescription;
    ContestType contestType;
    Integer totalMaxPoints;
    Integer passingScore;

    // Thông tin lớp
    UUID classId;
    String classCode;
    String className;

    // Thời gian thi
    Instant scheduledStartTime;
    Instant scheduledEndTime;

    // Thống kê
    Integer totalStudents;
    Integer submittedCount;
    Integer passedCount;
    Double averageScore;

    // Danh sách điểm
    List<GradebookEntryDTO> entries;
}
