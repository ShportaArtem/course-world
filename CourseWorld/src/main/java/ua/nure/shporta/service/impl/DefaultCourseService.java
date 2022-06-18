package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.nure.shporta.constants.CourseConstants;
import ua.nure.shporta.dao.CourseDAO;
import ua.nure.shporta.dao.UserDAO;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.exception.ExceptionMessages;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Lecture;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.CourseService;
import ua.nure.shporta.service.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultCourseService implements CourseService {
    @Value("${page.courses.size}")
    private Integer pageSize;

    @Autowired
    private UserService userService;
    @Autowired
    private CourseDAO courseDAO;

    @Override
    public void requestCourse(Integer courseId) {
        Course course = courseDAO.findById(courseId).get();
        course.setStatus(CourseConstants.CHANGED_STATUS);
        courseDAO.saveAndFlush(course);
    }

    @Override
    public void approveCourse(Integer courseId) {
        Course course = courseDAO.findById(courseId).get();
        course.setStatus(CourseConstants.APPROVED_STATUS);
        courseDAO.saveAndFlush(course);
    }

    @Override
    public void cancelCourse(Integer courseId, String comments) {
        Course course = courseDAO.findById(courseId).get();
        course.setStatus(CourseConstants.CANCELED_STATUS);
        course.setComments(comments);
        courseDAO.saveAndFlush(course);
    }

    @Override
    public Page<Course> findApprovedCoursesPageable(Optional<Integer> page) {
        int currentPage = page.orElse(1);

        return courseDAO.findAllByStatusEquals(PageRequest.of(currentPage - 1, this.pageSize), CourseConstants.APPROVED_STATUS);
    }

    @Override
    public Page<Course> findCoursesNeedToManagePageable(Optional<Integer> page) {
        List<String> statuses = Arrays.asList(CourseConstants.CREATED_STATUS, CourseConstants.CHANGED_STATUS);
        Page<Course> coursePage = courseDAO.findAllByStatusIn(PageRequest.of(page.orElse(1) - 1, this.pageSize), statuses);

        return coursePage;
    }

    @Override
    public Page<Course> findApprovedCoursesByNamePageable(Optional<Integer> page, String name) throws DBException {
        Page<Course> coursePage = courseDAO.findAllByNameContainingAndStatusEquals(name, CourseConstants.APPROVED_STATUS, PageRequest.of(page.orElse(1) - 1, this.pageSize));
        if (coursePage.getContent().isEmpty()) {
            throw new DBException(ExceptionMessages.NO_RESULTS_FOR_SEARCH);
        }
        return coursePage;
    }

    @Override
    public Page<Course> findCoursesByCreatorPageable(Optional<Integer> page, User creator) {
        Page<Course> coursePage = courseDAO.findAllByCreator(creator, PageRequest.of(page.orElse(1) - 1, this.pageSize));
        return coursePage;
    }

    @Override
    public Page<Course> findApprovedCoursesFirstPage() {
        Pageable findFirstPage = PageRequest.of(0, this.pageSize);

        return courseDAO.findAllByStatusEquals(findFirstPage,CourseConstants.APPROVED_STATUS);
    }

    @Override
    public Course findCourseById(int courseId) throws DBException {
        return courseDAO.findById(courseId).orElseThrow(() -> new DBException(ExceptionMessages.COURSE_NOT_FOUNDED));
    }

    @Override
    public Course createCourse(Course course) {
        course.setRate(0d);
        course.setNumberOfVotes(0);
        course.setStatus(CourseConstants.CREATED_STATUS);
        User user = userService.getCurrentUser();
        course.setCreator(user);
        return courseDAO.saveAndFlush(course);
    }

    @Override
    public Course updateCourse(Course course) {
        Course oldCourse = courseDAO.findById(course.getId()).get();

        return courseDAO.saveAndFlush(fulfillNotUpdatableFields(oldCourse, course));
    }

    @Override
    public Integer overallMark(Course course) {
        int mark = 0;
        for (Lecture lecture : course.getLectures()) {
            mark += lecture.getTest() != null ? lecture.getTest().getQuestions().size() : 0;
        }
        return mark;
    }

    @Override
    public Course voteCourse(Course course, Integer rate) {
        course.setSumRate(course.getSumRate() + rate);
        course.setNumberOfVotes(course.getNumberOfVotes() + 1);
        course.setRate(calculateRate(course));

        return courseDAO.saveAndFlush(course);
    }

    private Double calculateRate(Course course) {
        return BigDecimal.valueOf(Double.valueOf(course.getSumRate()) / course.getNumberOfVotes())
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .doubleValue();
    }

    private Course fulfillNotUpdatableFields(Course oldCourse, Course updatedCourse) {
        updatedCourse.setCreator(oldCourse.getCreator());
        updatedCourse.setRate(oldCourse.getRate());
        updatedCourse.setNumberOfVotes(oldCourse.getNumberOfVotes());
        updatedCourse.setSumRate(oldCourse.getSumRate());
        return updatedCourse;
    }
}
