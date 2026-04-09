package com.truongsonkmhd.unetistudy.repository.clazz;

import com.truongsonkmhd.unetistudy.model.User;
import com.truongsonkmhd.unetistudy.model.lesson.course_lesson.ClassContest;
import com.truongsonkmhd.unetistudy.model.lesson.course_lesson.ClassContestSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassContestSubmissionRepository extends JpaRepository<ClassContestSubmission, UUID> {
    
    Optional<ClassContestSubmission> findByUserAndClassContestAndStatus(User user, ClassContest classContest, String status);
    
    List<ClassContestSubmission> findByUserAndClassContestOrderByStartedAtDesc(User user, ClassContest classContest);
    
    long countByUserAndClassContest(User user, ClassContest classContest);

    /**
     * Lấy tất cả submission đã nộp (SUBMITTED) cho một class contest
     * Dùng cho bảng điểm (gradebook)
     */
    @Query("SELECT s FROM ClassContestSubmission s JOIN FETCH s.user WHERE s.classContest = :classContest AND s.status = 'SUBMITTED' ORDER BY s.totalScore DESC")
    List<ClassContestSubmission> findSubmittedByClassContest(@Param("classContest") ClassContest classContest);

    /**
     * Lấy bài nộp tốt nhất của mỗi user cho một class contest (điểm cao nhất)
     */
    @Query("""
        SELECT s FROM ClassContestSubmission s
        JOIN FETCH s.user
        WHERE s.classContest = :classContest
        AND s.status = 'SUBMITTED'
        AND s.totalScore = (
            SELECT MAX(s2.totalScore)
            FROM ClassContestSubmission s2
            WHERE s2.user = s.user
            AND s2.classContest = s.classContest
            AND s2.status = 'SUBMITTED'
        )
        ORDER BY s.totalScore DESC
        """)
    List<ClassContestSubmission> findBestSubmissionsByClassContest(@Param("classContest") ClassContest classContest);
}
