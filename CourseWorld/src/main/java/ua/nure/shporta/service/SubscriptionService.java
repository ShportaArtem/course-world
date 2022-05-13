package ua.nure.shporta.service;

import org.springframework.stereotype.Component;
import ua.nure.shporta.model.Subscription;

@Component
public interface SubscriptionService {
    Subscription findByUsersIdAndCourseId(Integer usersId, Integer courseId);

    void subscribe(Integer userId, Integer courseId);

    void startCourse(Integer userId, Integer courseId);

    void finishCourse(Integer userId, Integer courseId);

    void addMark(Integer userId, Integer courseId, Integer mark);

}
