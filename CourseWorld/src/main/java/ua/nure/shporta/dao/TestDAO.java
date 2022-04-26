package ua.nure.shporta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.shporta.model.Test;

@Repository
public interface TestDAO extends JpaRepository<Test, Integer> {
}
