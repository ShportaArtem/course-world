package ua.nure.shporta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.shporta.model.Test;
import ua.nure.shporta.model.User;
import ua.nure.shporta.model.UsersTest;

import java.util.Optional;

@Repository
public interface UsersTestDAO extends JpaRepository<UsersTest, Integer> {
    Optional<UsersTest> findUsersTestByUserAndTest(User user, Test test);
}
