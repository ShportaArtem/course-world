package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.shporta.dto.AnswerDTO;
import ua.nure.shporta.dto.AnswersDTO;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.model.Lecture;
import ua.nure.shporta.model.Question;
import ua.nure.shporta.model.Test;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.CourseService;
import ua.nure.shporta.service.LectureService;
import ua.nure.shporta.service.TestService;
import ua.nure.shporta.service.UserService;

import java.util.List;

import static ua.nure.shporta.controllers.ControllerConstants.*;

@Controller
@RequestMapping("/courses/{courseId}/lectures/test")
public class TestController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private TestService testService;
    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String addTest(@PathVariable Integer courseId, @RequestParam Integer lectureId, Model model) {
        model.addAttribute("test", new Test());
        return "addTest";
    }

    @PostMapping("/add")
    public String addTest(@PathVariable Integer courseId, @RequestParam Integer lectureId, @ModelAttribute Test test, Model model) {
        test.setLecture((Lecture) model.getAttribute(CURRENT_LECTURE_ATTRIBUTE));
        Test createdTest = testService.createTest(test);

        return "redirect:/courses/" + courseId + "/lectures/" + lectureId + "/test";
    }

    @GetMapping
    public String getTest(@PathVariable Integer courseId, @RequestParam Integer lectureId, Model model) {
        Lecture currentLecture = (Lecture) model.getAttribute(CURRENT_LECTURE_ATTRIBUTE);
        Test currentTest = testService.findTestById(currentLecture.getTest().getId());
        model.addAttribute(CURRENT_TEST_ATTRIBUTE, currentTest);
        model.addAttribute("answers", createAnswersDTO(currentTest.getQuestions()));
        model.addAttribute(CURRENT_USERS_TEST_ATTRIBUTE, testService.findUsersTestByUserAndTest((User)model.getAttribute(CURRENT_USER_ATTRIBUTE), currentTest));
        model.addAttribute(CURRENT_USER_ATTRIBUTE, userService.getCurrentUser());
        model.addAttribute("hasNext", lectureService.hasNextLecture(currentLecture));
        model.addAttribute("nextLecture", lectureService.findNextLecture(currentLecture));

        return "test";
    }

    @PostMapping
    public String finishTest(@PathVariable Integer courseId, @RequestParam Integer lectureId, @ModelAttribute AnswersDTO answers, Model model) {
        Lecture currentLecture = (Lecture) model.getAttribute(CURRENT_LECTURE_ATTRIBUTE);
        Test currentTest = testService.findTestById(currentLecture.getTest().getId());

        return "test";
    }

    private AnswersDTO createAnswersDTO(List<Question> questions){
        AnswersDTO answers = new AnswersDTO();
        for(Question question : questions){
            answers.addAnswer(new AnswerDTO());
        }
        return answers;
    }


    @ModelAttribute
    public void addAttributes(@PathVariable Integer courseId, @RequestParam Integer lectureId, Model model) throws DBException {
        model.addAttribute(CURRENT_COURSE_ATTRIBUTE, courseService.findCourseById(courseId));
        model.addAttribute(CURRENT_LECTURE_ATTRIBUTE, lectureService.findLectureById(lectureId));
    }
}
