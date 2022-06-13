package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.nure.shporta.dao.UserDAO;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.exception.ExceptionMessages;
import ua.nure.shporta.model.User;
import ua.nure.shporta.constants.UserConstants;
import ua.nure.shporta.service.UserService;

import java.util.Optional;

@Component
public class DefaultUserService implements UserService {

    @Value("${page.users.size}")
    private Integer pageSize;

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
    public User registerModerator(User user) throws DBException {
        if (userDAO.findByLogin(user.getLogin()).isPresent()) {
            throw new DBException(ExceptionMessages.ERR_NOT_UNIQUE_USER_LOGIN);
        }
        user.setRole(UserConstants.MODERATOR_ROLE);
        return userDAO.save(user);
    }

    @Override
    public User findUserByLogin(String login) {
        return userDAO.findByLogin(login).get();
    }

    @Override
    public User findUserById(Integer id) {
        return userDAO.findById(id).get();
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

    @Override
    public Page<User> findAllModerators(Optional<Integer> page) {
        int currentPage = page.orElse(1);

        return userDAO.findAllByRoleEquals(PageRequest.of(currentPage - 1, this.pageSize), UserConstants.MODERATOR_ROLE);
    }

    @Override
    public void deleteUser(Integer userId) {
        User userToRemove = userDAO.findById(userId).get();

        userDAO.delete(userToRemove);
    }
}
