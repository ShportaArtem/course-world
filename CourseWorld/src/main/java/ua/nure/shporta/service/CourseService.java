package ua.nure.shporta.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.model.Course;

import java.util.Optional;

@Component
public interface CourseService {
    Page<Course> findCoursesPageable(Optional<Integer> page);

    Page<Course> findCoursesByNamePageable(Optional<Integer> page, String name) throws DBException;

    Page<Course> findCoursesFirstPage();

    Course findCourseById(int courseId) throws DBException;

    Course createCourse(Course course);

    Course updateCourse(Course course);

    Integer overallMark(Course course);

    Course voteCourse(Course course, Integer rate);
}
