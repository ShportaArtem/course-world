package ua.nure.shporta.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.shporta.model.User;

import java.util.Optional;

@Repository
public interface UserDAO extends CrudRepository<User, Integer> {
    Optional<User> findByLogin(String login);

    Page<User> findAllByRoleEquals(Pageable pageable, String role);
}
