package ua.nure.shporta.service;

import org.springframework.stereotype.Component;
import ua.nure.shporta.dto.AnswersDTO;
import ua.nure.shporta.model.Question;
import ua.nure.shporta.model.Test;
import ua.nure.shporta.model.User;
import ua.nure.shporta.model.UsersTest;

import java.util.List;

@Component
public interface TestService {
    Test createTest(Test test);

    Test findTestById(Integer id);

    UsersTest findUsersTestByUserAndTest(User user, Test test);

    Integer checkTest(AnswersDTO answers, List<Question> questions);

    UsersTest finishTest(User user, Test test, Integer mark);
}
