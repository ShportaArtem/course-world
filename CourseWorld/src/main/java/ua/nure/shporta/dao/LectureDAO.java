package ua.nure.shporta.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Lecture;

import java.util.Optional;

@Repository
public interface LectureDAO extends JpaRepository<Lecture, Integer> {
    Page<Lecture> findAllByCourseOrderByPositionAsc(Course course, Pageable pageable);

    Optional<Lecture> findLectureByCourseAndPosition(Course course, Integer position);
}
