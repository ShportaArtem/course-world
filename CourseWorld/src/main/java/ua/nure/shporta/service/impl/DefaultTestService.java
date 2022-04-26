package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.nure.shporta.dao.TestDAO;
import ua.nure.shporta.dao.UsersTestDAO;
import ua.nure.shporta.dto.AnswerDTO;
import ua.nure.shporta.dto.AnswersDTO;
import ua.nure.shporta.model.Question;
import ua.nure.shporta.model.Test;
import ua.nure.shporta.model.User;
import ua.nure.shporta.model.UsersTest;
import ua.nure.shporta.service.TestService;

import java.util.List;

@Component
public class DefaultTestService implements TestService {
    @Autowired
    private TestDAO testDAO;
    @Autowired
    private UsersTestDAO usersTestDAO;

    @Override
    public Test createTest(Test test) {
        return testDAO.saveAndFlush(test);
    }

    @Override
    public Test findTestById(Integer id) {
        return testDAO.getById(id);
    }

    @Override
    public UsersTest findUsersTestByUserAndTest(User user, Test test) {
        return usersTestDAO.findUsersTestByUserAndTest(user, test).orElse(null);
    }

    @Override
    public Integer checkTest(AnswersDTO answers, List<Question> questions) {
        Integer mark = 0;
        for(Question question : questions){
            AnswerDTO answer = answers.getAnswers().stream().filter(a -> a.getQuestion().equals(question.getQuestionText())).findFirst().get();
            switch (question.getCorrectAnswer()){
                case 1:
                    if(question.getFirstAnswer().equals(answer.getAnswer())){
                        mark++;
                    }
                    break;
                case 2:
                    if(question.getSecondAnswer().equals(answer.getAnswer())){
                        mark++;
                    }
                    break;
                case 3:
                    if(question.getThirdAnswer().equals(answer.getAnswer())){
                        mark++;
                    }
                    break;
                default:
                    if(question.getFourthAnswer().equals(answer.getAnswer())){
                        mark++;
                    }
                    break;
            }
        }
        return null;
    }


}
