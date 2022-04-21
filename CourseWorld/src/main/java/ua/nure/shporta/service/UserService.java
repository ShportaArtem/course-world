package ua.nure.shporta.service;

import org.springframework.stereotype.Component;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.model.User;

@Component
public interface UserService {
    User register(User user) throws DBException;

    User findUserByLogin(String login);

    boolean isSubscribedByCourseId(User user, Integer courseId);

    User getCurrentUser();
}
