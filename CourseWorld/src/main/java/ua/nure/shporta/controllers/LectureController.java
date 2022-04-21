package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.helper.Helper;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Lecture;
import ua.nure.shporta.service.CourseService;
import ua.nure.shporta.service.LectureService;
import ua.nure.shporta.service.UserService;

import java.util.List;
import java.util.Optional;

import static ua.nure.shporta.controllers.ControllerConstants.*;


@Controller
@RequestMapping("/courses/{courseId}/lectures")
public class LectureController {

    @Autowired
    private Helper helper;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getLectures(@RequestParam(value = "page", required = false) Optional<Integer> page,
                              @PathVariable Integer courseId, Model model) throws DBException {

        Page<Lecture> lecturePage = lectureService.findLecturesByCourseId(page, courseId);
        model.addAttribute("lecturePage", lecturePage);
        if (lecturePage.getTotalPages() > 0) {
            model.addAttribute("pageNumbers", helper.getPages(lecturePage.getTotalPages()));
        }
        model.addAttribute(CURRENT_USER_ATTRIBUTE, userService.getCurrentUser());
        return "lectures";
    }

    @GetMapping("/add")
    public String addLecture(@PathVariable Integer courseId, Model model) {
        Course currentCourse = (Course) model.getAttribute(CURRENT_COURSE_ATTRIBUTE);
        model.addAttribute("lecture", new Lecture());
        List<Lecture> lectures = currentCourse.getLectures();
        if (lectures == null) {
            model.addAttribute("maxPosition", 1);
        } else {
            model.addAttribute("maxPosition", lectures.size() + 1);
        }
        return "addLecture";
    }

    @PostMapping("/add")
    public String addLecture(@PathVariable Integer courseId, @ModelAttribute Lecture lecture, Model model) {
        lecture.setCourse((Course) model.getAttribute(CURRENT_COURSE_ATTRIBUTE));
        lectureService.createLecture(lecture);
        return "redirect:/courses/" + courseId + "/lectures";
    }

    @GetMapping("/{id}")
    public String getLecture(@PathVariable Integer courseId, @PathVariable Integer id, Model model) {
        Lecture currentLecture = lectureService.findLectureById(id);
        model.addAttribute("currentLecture", currentLecture);
        model.addAttribute("hasNext", lectureService.hasNextLecture(currentLecture));
        model.addAttribute("hasPrevious", lectureService.hasPreviousLecture(currentLecture));
        model.addAttribute(CURRENT_USER_ATTRIBUTE, userService.getCurrentUser());
        model.addAttribute("nextLecture", lectureService.findNextLecture(currentLecture));
        model.addAttribute("previousLecture", lectureService.findPreviousLecture(currentLecture));
        return "lecture";
    }


    @ModelAttribute
    public void addAttributes(@PathVariable Integer courseId, Model model) throws DBException {
        model.addAttribute(CURRENT_COURSE_ATTRIBUTE, courseService.findCourseById(courseId));
    }
}
