package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.helper.Helper;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.CourseService;
import ua.nure.shporta.service.SubscriptionService;
import ua.nure.shporta.service.UserService;

import java.util.Optional;

import static ua.nure.shporta.controllers.ControllerConstants.CURRENT_COURSE_ATTRIBUTE;
import static ua.nure.shporta.controllers.ControllerConstants.CURRENT_USER_ATTRIBUTE;

@Controller
@RequestMapping("/manage/courses")
public class ModeratorCourseController {
    @Autowired
    private Helper helper;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listRequiredToManageCourses(@RequestParam("page") Optional<Integer> page, Model model){
        Page<Course> coursePage = courseService.findCoursesNeedToManagePageable(page);
        model.addAttribute("coursePage", coursePage);

        if (coursePage.getTotalPages() > 0) {
            model.addAttribute("pageNumbers", helper.getPages(coursePage.getTotalPages()));
        }
        return "manageCourses";
    }

    @GetMapping("/{id}")
    public String getCourse(@PathVariable(name = "id") int courseId, Model model) throws DBException {

        Course course = courseService.findCourseById(courseId);
        model.addAttribute(CURRENT_COURSE_ATTRIBUTE, course);
        User user = userService.getCurrentUser();
        model.addAttribute(CURRENT_USER_ATTRIBUTE, user);
        return "manageCourse";
    }

    @PostMapping("/{id}/approve")
    public String approveCourse(@PathVariable(name = "id") int courseId, Model model) throws DBException {

        courseService.approveCourse(courseId);

        return "redirect:/manage/courses/";
    }

    @PostMapping("/{id}/cancel")
    public String cancelCourse(@PathVariable(name = "id") int courseId, String comments, Model model) throws DBException {

        courseService.cancelCourse(courseId, comments);

        return "redirect:/manage/courses/";
    }

    @GetMapping("/{id}/cancel")
    public String getCancelCourseForm(@PathVariable(name = "id") int courseId, Model model) throws DBException {
        Course course = courseService.findCourseById(courseId);
        model.addAttribute("course", course);
        return "cancelCourse";
    }
}
