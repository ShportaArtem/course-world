package ua.nure.shporta.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Subscription;
import ua.nure.shporta.model.User;

import java.util.Optional;

@Repository
public interface SubscriptionDAO extends JpaRepository<Subscription, Integer> {
    Optional<Subscription> findByUserAndCourse(User user, Course course);

    Page<Subscription> findAllByUser(User user, Pageable pageable);
}
