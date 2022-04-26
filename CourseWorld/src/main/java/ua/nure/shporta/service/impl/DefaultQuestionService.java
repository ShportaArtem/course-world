package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.nure.shporta.dao.QuestionDAO;
import ua.nure.shporta.model.Question;
import ua.nure.shporta.service.QuestionService;

@Component
public class DefaultQuestionService implements QuestionService {
    @Autowired
    private QuestionDAO questionDAO;

    @Override
    public Question createQuestion(Question question) {
        return questionDAO.saveAndFlush(question);
    }
}
