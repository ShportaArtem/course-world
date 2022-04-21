package ua.nure.shporta.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.shporta.model.Course;

@Repository
public interface CourseDAO extends JpaRepository<Course, Integer> {
    @Override
    Page<Course> findAll(Pageable pageable);

    Page<Course> findAllByNameContaining(String infix, Pageable pageable);

}
