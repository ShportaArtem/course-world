package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.nure.shporta.dao.SubscriptionDAO;
import ua.nure.shporta.dao.TestDAO;
import ua.nure.shporta.dao.UsersTestDAO;
import ua.nure.shporta.dto.AnswerDTO;
import ua.nure.shporta.dto.AnswersDTO;
import ua.nure.shporta.model.*;
import ua.nure.shporta.service.SubscriptionService;
import ua.nure.shporta.service.TestService;

import java.util.List;

@Component
public class DefaultTestService implements TestService {
    @Autowired
    private TestDAO testDAO;
    @Autowired
    private UsersTestDAO usersTestDAO;
    @Autowired
    private SubscriptionService subscriptionService;

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
        for (Question question : questions) {
            AnswerDTO answer = answers.getAnswers().stream().filter(a -> a.getQuestion().equals(question.getQuestionText())).findFirst().get();
            if (question.getCorrectAnswer().equals(answer.getAnswer())) {
                mark++;
            }
        }
        return mark;
    }

    @Override
    public UsersTest finishTest(User user, Test test, Integer mark) {
        UsersTest usersTest = findUsersTestByUserAndTest(user, test);
        boolean changes = false;
        Integer oldMark = null;
        if (usersTest != null) {
            usersTest.setAttempts(usersTest.getAttempts() + 1);
            changes = true;
            oldMark = usersTest.getMark();
            if (usersTest.getMark() < mark) {
                usersTest.setMark(mark);
            }
        } else {
            usersTest = buildUsersTest(user, test, mark);
        }

        subscriptionService.addMark(user.getId(), test.getLecture().getCourse().getId(), mark, changes, oldMark);
        usersTest = usersTestDAO.saveAndFlush(usersTest);
        return usersTest;
    }

    protected UsersTest buildUsersTest(User user, Test test, Integer mark) {
        UsersTest usersTest = new UsersTest();
        usersTest.setTest(test);
        usersTest.setUser(user);
        usersTest.setAttempts(1);
        usersTest.setMark(mark);
        return usersTest;
    }
}
