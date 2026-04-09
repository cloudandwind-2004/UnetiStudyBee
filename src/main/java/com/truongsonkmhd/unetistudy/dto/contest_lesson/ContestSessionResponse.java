package com.truongsonkmhd.unetistudy.dto.contest_lesson;

import com.truongsonkmhd.unetistudy.common.ContestType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContestSessionResponse {
    UUID submissionId;
    UUID classContestId;
    String title;
    String description;
    ContestType contestType;
    Instant startTime;
    Instant endTime;
    Long durationMinutes;
    Long timeLeftSeconds;
    String status;
    List<ContestItemDTO> items;
}
