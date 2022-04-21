package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.nure.shporta.dao.CourseDAO;
import ua.nure.shporta.dao.UserDAO;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.exception.ExceptionMessages;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.CourseService;
import ua.nure.shporta.service.UserService;

import java.util.Optional;

@Component
public class DefaultCourseService implements CourseService {
    @Value("${page.courses.size}")
    private Integer pageSize;

    @Autowired
    private UserService userService;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public Page<Course> findCoursesPageable(Optional<Integer> page) {
        int currentPage = page.orElse(1);
        return courseDAO.findAll(PageRequest.of(currentPage - 1, this.pageSize));
    }

    @Override
    public Page<Course> findCoursesByNamePageable(Optional<Integer> page, String name) throws DBException {
        Page<Course> coursePage = courseDAO.findAllByNameContaining(name, PageRequest.of(page.orElse(1) - 1, this.pageSize));
        if (coursePage.getContent().isEmpty()) {
            throw new DBException(ExceptionMessages.NO_RESULTS_FOR_SEARCH);
        }
        return coursePage;
    }

    @Override
    public Page<Course> findCoursesFirstPage() {
        Pageable findFirstPage = PageRequest.of(0, this.pageSize);

        return courseDAO.findAll(findFirstPage);
    }

    @Override
    public Course findCourseById(int courseId) throws DBException {
        return courseDAO.findById(courseId).orElseThrow(() -> new DBException(ExceptionMessages.COURSE_NOT_FOUNDED));
    }

    @Override
    public Course createCourse(Course course) {
        course.setRate(0d);
        course.setNumberOfVotes(0);
        User user = userService.getCurrentUser();
        course.setCreator(user);
        return courseDAO.saveAndFlush(course);
    }

    @Override
    public Course updateCourse(Course course) {
        Course oldCourse = courseDAO.findById(course.getId()).get();

        return courseDAO.saveAndFlush(fulfillNotUpdatableFields(oldCourse, course));
    }

    private Course fulfillNotUpdatableFields(Course oldCourse, Course updatedCourse){
        updatedCourse.setCreator(oldCourse.getCreator());
        updatedCourse.setRate(oldCourse.getRate());
        updatedCourse.setNumberOfVotes(oldCourse.getNumberOfVotes());
        return updatedCourse;
    }
}
