package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.nure.shporta.dao.UserDAO;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.exception.ExceptionMessages;
import ua.nure.shporta.model.User;
import ua.nure.shporta.constants.UserConstants;
import ua.nure.shporta.service.UserService;

@Component
public class DefaultUserService implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User register(User user) throws DBException {
        if (userDAO.findByLogin(user.getLogin()).isPresent()) {
            throw new DBException(ExceptionMessages.ERR_NOT_UNIQUE_USER_LOGIN);
        }
        user.setRole(UserConstants.USER_ROLE);
        return userDAO.save(user);
    }

    @Override
    public User findUserByLogin(String login) {
        return userDAO.findByLogin(login).get();
    }

    @Override
    public boolean isSubscribedByCourseId(User user, Integer courseId) {

        return userDAO.findById(user.getId())
                .get().getSubscriptions()
                .stream()
                .anyMatch(subscription -> subscription.getCourse().getId().equals(courseId));
    }

    @Override
    public User getCurrentUser() {
        return findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
