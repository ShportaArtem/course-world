package ua.nure.shporta.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.model.Subscription;
import ua.nure.shporta.model.User;

import java.util.Optional;

@Component
public interface SubscriptionService {
    Subscription findByUsersIdAndCourseId(Integer usersId, Integer courseId);

    void subscribe(Integer userId, Integer courseId);

    void startCourse(Integer userId, Integer courseId);

    void finishCourse(Integer userId, Integer courseId);

    void addMark(Integer userId, Integer courseId, Integer mark, boolean changes, Integer oldMark);

    void voteCourse(Integer userId, Integer courseId);

    Page<Subscription> findSubscriptionsByUserPage(Optional<Integer> page, User user) throws DBException;
}
