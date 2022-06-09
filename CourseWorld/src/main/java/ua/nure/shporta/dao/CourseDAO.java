package ua.nure.shporta.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.User;

import java.util.List;

@Repository
public interface CourseDAO extends JpaRepository<Course, Integer> {
    Page<Course> findAllByStatusEquals(Pageable pageable, String status);

    Page<Course> findAllByStatusIn(Pageable pageable, List<String> statuses);

    Page<Course> findAllByNameContainingAndStatusEquals(String infix, String status, Pageable pageable);

    Page<Course> findAllByCreator(User creator, Pageable pageable);
}
