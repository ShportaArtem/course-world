package ua.nure.shporta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nure.shporta.model.Question;

public interface QuestionDAO extends JpaRepository<Question, Integer> {
}
