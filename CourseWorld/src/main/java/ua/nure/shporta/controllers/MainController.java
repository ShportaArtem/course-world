package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.nure.shporta.helper.Helper;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.service.CourseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private Helper helper;

    @GetMapping("/")
    public String home(Model model){
       Page<Course> page = courseService.findApprovedCoursesFirstPage();
        model.addAttribute("coursePage", page);

        if(page.getTotalPages()>0){
            model.addAttribute("pageNumbers", helper.getPages(page.getTotalPages()));
        }
        return "home";
    }

    @GetMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
}
