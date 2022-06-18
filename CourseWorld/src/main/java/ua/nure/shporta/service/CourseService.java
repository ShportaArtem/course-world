package ua.nure.shporta.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.User;

import java.util.Optional;

@Component
public interface CourseService {
    void requestCourse(Integer courseId);

    void approveCourse(Integer courseId);

    void cancelCourse(Integer courseId, String comments);

    Page<Course> findApprovedCoursesPageable(Optional<Integer> page);

    Page<Course> findCoursesNeedToManagePageable(Optional<Integer> page);

    Page<Course> findApprovedCoursesByNamePageable(Optional<Integer> page, String name) throws DBException;

    Page<Course> findCoursesByCreatorPageable(Optional<Integer> page, User creator);

    Page<Course> findApprovedCoursesFirstPage();

    Course findCourseById(int courseId) throws DBException;

    Course createCourse(Course course);

    Course updateCourse(Course course);

    Integer overallMark(Course course);

    Course voteCourse(Course course, Integer rate);
}
