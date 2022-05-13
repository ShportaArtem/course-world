package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.model.Lecture;
import ua.nure.shporta.model.Question;
import ua.nure.shporta.service.CourseService;
import ua.nure.shporta.service.LectureService;
import ua.nure.shporta.service.QuestionService;

import static ua.nure.shporta.controllers.ControllerConstants.CURRENT_COURSE_ATTRIBUTE;
import static ua.nure.shporta.controllers.ControllerConstants.CURRENT_LECTURE_ATTRIBUTE;

@Controller
@RequestMapping("/courses/{courseId}/lectures/test/questions")
public class QuestionsController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/add")
    public String addQuestion(@PathVariable Integer courseId, @RequestParam Integer lectureId, Model model) {
        model.addAttribute("newQuestion", new Question());

        return "addQuestion";
    }

    @PostMapping("/add")
    public String addQuestion(@PathVariable Integer courseId, @RequestParam Integer lectureId, @ModelAttribute Question newQuestion, @RequestParam Integer correctAnswer, Model model) {
        newQuestion.setTest(((Lecture) model.getAttribute(CURRENT_LECTURE_ATTRIBUTE)).getTest());
        setCorrectAnswer(newQuestion, correctAnswer);
        questionService.createQuestion(newQuestion);

        return "redirect:/courses/" + courseId + "/lectures/test?lectureId=" + lectureId;
    }

    @ModelAttribute
    public void addAttributes(@PathVariable Integer courseId, @RequestParam Integer lectureId, Model model) throws DBException {
        model.addAttribute(CURRENT_COURSE_ATTRIBUTE, courseService.findCourseById(courseId));
        model.addAttribute(CURRENT_LECTURE_ATTRIBUTE, lectureService.findLectureById(lectureId));
    }

    private void setCorrectAnswer(Question question, Integer correctAnswer) {
        switch (correctAnswer) {
            case 1:
                question.setCorrectAnswer(question.getFirstAnswer());
                break;
            case 2:
                question.setCorrectAnswer(question.getSecondAnswer());
                break;
            case 3:
                question.setCorrectAnswer(question.getThirdAnswer());
                break;
            default:
                question.setCorrectAnswer(question.getFourthAnswer());
                break;
        }
    }
}
