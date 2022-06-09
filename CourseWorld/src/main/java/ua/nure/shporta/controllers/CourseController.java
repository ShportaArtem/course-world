package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.helper.Helper;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Subscription;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.CourseService;
import ua.nure.shporta.service.SubscriptionService;
import ua.nure.shporta.service.UserService;

import java.util.Optional;

import static ua.nure.shporta.controllers.ControllerConstants.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private Helper helper;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public String listCourses(@RequestParam("page") Optional<Integer> page, Model model) {

        Page<Course> coursePage = courseService.findApprovedCoursesPageable(page);
        model.addAttribute("coursePage", coursePage);

        if (coursePage.getTotalPages() > 0) {
            model.addAttribute("pageNumbers", helper.getPages(coursePage.getTotalPages()));
        }
        return "home";
    }

    @GetMapping("/search")
    public String findCourses(@RequestParam(value = "page", required = false) Optional<Integer> page,
                              @RequestParam("name") String name, Model model) {

        Page<Course> coursePage = null;
        try {
            coursePage = courseService.findApprovedCoursesByNamePageable(page, name);
        } catch (DBException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
        model.addAttribute("coursePage", coursePage);

        if (coursePage.getTotalPages() > 0) {
            model.addAttribute("pageNumbers", helper.getPages(coursePage.getTotalPages()));
            model.addAttribute("searchedName", name);
        }
        return "home";
    }

    @GetMapping("/{id}")
    public String getCourse(@PathVariable(name = "id") int courseId, Model model) throws DBException {

        Course course = courseService.findCourseById(courseId);
        model.addAttribute(CURRENT_COURSE_ATTRIBUTE, course);
        User user = userService.getCurrentUser();
        model.addAttribute(CURRENT_USER_ATTRIBUTE, user);
        model.addAttribute("overallMark", courseService.overallMark(course));
        boolean isSubscribed = userService.isSubscribedByCourseId(user, courseId);
        model.addAttribute("isSubscribed", isSubscribed);
        if (isSubscribed) {
            model.addAttribute("subscription", subscriptionService.findByUsersIdAndCourseId(user.getId(), courseId));
        }
        return "course";
    }

    @GetMapping("/{id}/edit")
    public String editCourse(@PathVariable(name = "id") int courseId, Model model) throws DBException {

        Course course = courseService.findCourseById(courseId);
        model.addAttribute(CURRENT_COURSE_ATTRIBUTE, course);

        return "editCourse";
    }

    @PostMapping("/{id}/request")
    public String requestCourse(@PathVariable(name = "id") int courseId, Model model) throws DBException {

        courseService.requestCourse(courseId);

        return "redirect:/courses/" + courseId;
    }

    @PostMapping("/{id}/edit")
    public String editCourse(@ModelAttribute(name = CURRENT_COURSE_ATTRIBUTE) Course course, Model model) {

        Course updatedCourse = courseService.updateCourse(course);

        return "redirect:/courses/" + updatedCourse.getId();
    }

    @PostMapping("/{id}/vote")
    public String voteCourse(@PathVariable(name = "id") int courseId, @RequestParam Integer rating, Model model) throws DBException {

        Course votedCourse = courseService.findCourseById(courseId);
        User user = userService.getCurrentUser();
        courseService.voteCourse(votedCourse, rating);
        subscriptionService.voteCourse(user.getId(), courseId);
        return "redirect:/courses/" + courseId;
    }

    @GetMapping("/add")
    public String getCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "addCourse";
    }

    @PostMapping("/add")
    public String createCourse(@ModelAttribute Course course, Model model) {
        courseService.createCourse(course);
        return "redirect:/";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam("courseId") Integer courseId, @RequestParam("userId") Integer userId, Model model) {
        subscriptionService.subscribe(userId, courseId);
        return "redirect:/courses/" + courseId;
    }

    @GetMapping("/{id}/start")
    public String startCourse(@PathVariable Integer id, Model model) {
        subscriptionService.startCourse(userService.getCurrentUser().getId(), id);
        return "redirect:/courses/" + id + "/lectures";
    }

    @GetMapping("/{id}/finish")
    public String finishCourse(@PathVariable Integer id, Model model) {
        subscriptionService.finishCourse(userService.getCurrentUser().getId(), id);
        return "redirect:/courses/" + id;
    }

    @GetMapping("/mylearning")
    public String listMyLearning(@RequestParam(value = "page", required = false) Optional<Integer> page, Model model) {
        User user = userService.getCurrentUser();
        Page<Subscription> foundedPage = null;
        try {
            foundedPage = subscriptionService.findSubscriptionsByUserPage(page, user);
        } catch (DBException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }
        model.addAttribute("coursePage", foundedPage);

        if (foundedPage.getTotalPages() > 0) {
            model.addAttribute("pageNumbers", helper.getPages(foundedPage.getTotalPages()));
        }
        return "myLearning";
    }

    @GetMapping("/my")
    public String listMyLearningCourses(@RequestParam(value = "page", required = false) Optional<Integer> page, Model model) {
        User user = userService.getCurrentUser();
        Page<Course> foundedPage = courseService.findCoursesByCreatorPageable(page, user);
        model.addAttribute("coursePage", foundedPage);

        if (foundedPage.getTotalPages() > 0) {
            model.addAttribute("pageNumbers", helper.getPages(foundedPage.getTotalPages()));
        }
        return "myCourses";
    }
}
