package com.truongsonkmhd.unetistudy.dto.contest_lesson;


import com.truongsonkmhd.unetistudy.common.ContestType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestInfo {
    UUID contestLessonId;
    String title; // Từ CourseLesson
    String description;
    ContestType contestType;
    Integer defaultTotalPoints;
    Integer codingExerciseCount;
    Integer quizQuestionCount;
}