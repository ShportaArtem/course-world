package ua.nure.shporta.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ua.nure.shporta.dao.QuestionDAO;
import ua.nure.shporta.model.Question;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultQuestionServiceTest {
    @InjectMocks
    private DefaultQuestionService testedInstance;

    private Question question;
    @Mock
    private QuestionDAO questionDAO;

    @Before
    public void setUp() {
        question = new Question();
    }

    @Test
    public void shouldCreateQuestionTest() {
        testedInstance.createQuestion(question);

        verify(questionDAO, times(1)).saveAndFlush(question);
    }
}