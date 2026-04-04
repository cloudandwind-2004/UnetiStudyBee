package com.truongsonkmhd.unetistudy.service.impl.course;

import com.truongsonkmhd.unetistudy.cache.CacheConstants;
import com.truongsonkmhd.unetistudy.context.UserContext;
import com.truongsonkmhd.unetistudy.dto.course_dto.CourseCardResponse;
import com.truongsonkmhd.unetistudy.dto.a_common.CursorResponse;
import com.truongsonkmhd.unetistudy.dto.a_common.PageResponse;
import com.truongsonkmhd.unetistudy.repository.course.CourseRepository;
import com.truongsonkmhd.unetistudy.repository.course.LessonProgressRepository;
import com.truongsonkmhd.unetistudy.service.CourseCatalogService;
import com.truongsonkmhd.unetistudy.cache.service.CourseCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service quản lý Catalog khóa học với tích hợp Caching
 * 
 * Cache Patterns áp dụng:
 * 1. Cache-Aside - Cache danh sách courses đã publish
 * 2. Time-based Expiration - TTL 15 phút cho catalog
 * 3. LRU Eviction - Tự động loại bỏ các pages ít truy cập
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseCatalogServiceImpl implements CourseCatalogService {

    private final CourseRepository courseRepository;
    private final CourseCacheService courseCacheService;
    private final LessonProgressRepository lessonProgressRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CourseCardResponse> getPublishedCourses(int page, int size, String q, String category) {
        // Chỉ cache phần data chung (không phụ thuộc user)
        PageResponse<CourseCardResponse> cached = courseCacheService.getCourseCatalog(page, size, q, "PUBLISHED", category,
                () -> queryPublishedCourses(page, size, q, category));

        // Luôn tính progress sau cache vì đây là dữ liệu riêng per-user
        enrichWithUserProgress(cached.getItems());
        return cached;
    }

    @Transactional(readOnly = true)
    public PageResponse<CourseCardResponse> queryPublishedCourses(int page, int size, String q, String category) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        Pageable pageable = PageRequest.of(safePage, safeSize);
        Page<CourseCardResponse> result = courseRepository.findPublishedCourseCardsWithFilters(
                (q != null && !q.isBlank()) ? q.trim() : null,
                (category != null && !category.isBlank()) ? category.trim() : null,
                pageable);

        return PageResponse.<CourseCardResponse>builder()
                .items(result.getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .hasNext(result.hasNext())
                .build();
    }

    /**
     * Enrich progressPercentage cho user hiện tại — luôn chạy, kể cả khi cache HIT.
     * progressPercentage là dữ liệu per-user nên không được lưu vào cache.
     */
    private void enrichWithUserProgress(List<CourseCardResponse> items) {
        UUID userId = UserContext.getUserID();
        if (userId == null || items == null || items.isEmpty()) return;

        List<UUID> courseIds = items.stream()
                .map(CourseCardResponse::getCourseId)
                .collect(Collectors.toList());

        Map<UUID, Long> totalLessonsMap = lessonProgressRepository.countLessonsPerCourse(courseIds)
                .stream()
                .collect(Collectors.toMap(r -> (UUID) r[0], r -> (Long) r[1]));

        Map<UUID, Long> completedMap = lessonProgressRepository
                .countCompletedPerStudentPerCourse(List.of(userId), courseIds)
                .stream()
                .collect(Collectors.toMap(r -> (UUID) r[1], r -> (Long) r[2]));

        items.forEach(card -> {
            long total = totalLessonsMap.getOrDefault(card.getCourseId(), 0L);
            long completed = completedMap.getOrDefault(card.getCourseId(), 0L);
            card.setProgressPercentage(total > 0 ? (completed * 100.0 / total) : null);
        });
    }

}