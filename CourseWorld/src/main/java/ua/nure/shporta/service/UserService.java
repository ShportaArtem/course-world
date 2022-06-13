package ua.nure.shporta.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.model.User;

import java.util.Optional;

@Component
public interface UserService {
    User register(User user) throws DBException;

    User registerModerator(User user) throws DBException;

    User findUserByLogin(String login);

    User findUserById(Integer id);

    boolean isSubscribedByCourseId(User user, Integer courseId);

    User getCurrentUser();

    Page<User> findAllModerators(Optional<Integer> page);

    void deleteUser(Integer userId);
}
