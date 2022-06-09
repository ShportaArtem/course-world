package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ua.nure.shporta.constants.SubscriptionConstants;
import ua.nure.shporta.dao.CourseDAO;
import ua.nure.shporta.dao.SubscriptionDAO;
import ua.nure.shporta.dao.UserDAO;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.exception.ExceptionMessages;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Subscription;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.SubscriptionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultSubscriptionService implements SubscriptionService {
    @Value("${page.courses.size}")
    private Integer pageSize;

    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SubscriptionDAO subscriptionDAO;

    @Override
    public Subscription findByUsersIdAndCourseId(Integer userId, Integer courseId) {
        User user = userDAO.findById(userId).get();
        Course course = courseDAO.findById(courseId).get();
        return subscriptionDAO.findByUserAndCourse(user, course).get();
    }

    @Override
    public void subscribe(Integer userId, Integer courseId) {
        User user = userDAO.findById(userId).get();
        Course course = courseDAO.findById(courseId).get();
        subscriptionDAO.saveAndFlush(build(user, course));
    }

    @Override
    public void startCourse(Integer userId, Integer courseId) {
        Subscription subscription = findByUsersIdAndCourseId(userId, courseId);
        subscription.setStatus(SubscriptionConstants.STARTED_STATUS);
        subscriptionDAO.saveAndFlush(subscription);
    }

    @Override
    public void finishCourse(Integer userId, Integer courseId) {
        Subscription subscription = findByUsersIdAndCourseId(userId, courseId);
        subscription.setStatus(SubscriptionConstants.FINISHED_STATUS);
        subscriptionDAO.saveAndFlush(subscription);
    }

    @Override
    public void addMark(Integer userId, Integer courseId, Integer mark, boolean changes, Integer oldMark) {
        Subscription subscription = findByUsersIdAndCourseId(userId, courseId);
        if (changes) {
            subscription.setCurrentMark(subscription.getCurrentMark() - oldMark + mark);
        } else {
            subscription.setCurrentMark(subscription.getCurrentMark() + mark);
        }
        subscriptionDAO.saveAndFlush(subscription);
    }

    @Override
    public void voteCourse(Integer userId, Integer courseId) {
        Subscription subscription = findByUsersIdAndCourseId(userId, courseId);
        subscription.setVoted(true);
        subscriptionDAO.saveAndFlush(subscription);
    }

    @Override
    public Page<Subscription> findSubscriptionsByUserPage(Optional<Integer> page, User user) throws DBException {
        Page<Subscription> foundedPage = subscriptionDAO.findAllByUser(user, PageRequest.of(page.orElse(1) - 1, this.pageSize));

        return foundedPage;
    }

    private Subscription build(User user, Course course) {
        Subscription subscription = new Subscription();
        subscription.setCurrentMark(0);
        subscription.setVoted(false);
        subscription.setStatus(SubscriptionConstants.SUBSCRIBED_STATUS);
        subscription.setCourse(course);
        subscription.setUser(user);

        return subscription;
    }
}
