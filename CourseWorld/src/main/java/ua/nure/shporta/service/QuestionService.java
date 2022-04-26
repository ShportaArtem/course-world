package ua.nure.shporta.service;

import org.springframework.stereotype.Component;
import ua.nure.shporta.model.Question;

@Component
public interface QuestionService {
    Question createQuestion(Question question);
}
