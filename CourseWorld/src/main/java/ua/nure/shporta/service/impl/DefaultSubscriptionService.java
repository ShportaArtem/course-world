package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.nure.shporta.constants.SubscriptionConstants;
import ua.nure.shporta.dao.CourseDAO;
import ua.nure.shporta.dao.SubscriptionDAO;
import ua.nure.shporta.dao.UserDAO;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Subscription;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.SubscriptionService;

@Component
public class DefaultSubscriptionService implements SubscriptionService {
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

    private Subscription build(User user, Course course){
        Subscription subscription = new Subscription();
        subscription.setCurrentMark(0);
        subscription.setStatus(SubscriptionConstants.SUBSCRIBED_STATUS);
        subscription.setCourse(course);
        subscription.setUser(user);

        return subscription;
    }
}
