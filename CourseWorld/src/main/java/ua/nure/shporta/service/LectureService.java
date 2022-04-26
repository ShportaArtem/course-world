package ua.nure.shporta.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Lecture;

import java.util.Optional;

@Component
public interface LectureService {
    Page<Lecture> findLecturesByCourseId(Optional<Integer> page, Integer courseId);

    Lecture createLecture(Lecture lecture);

    Lecture findLectureById(Integer id);

    Lecture findLectureByCourseAndPosition(Course course, Integer position);

    boolean hasPreviousLecture(Lecture lecture);

    boolean hasNextLecture(Lecture lecture);

    Lecture findNextLecture(Lecture lecture);

    Lecture findPreviousLecture(Lecture lecture);
}
