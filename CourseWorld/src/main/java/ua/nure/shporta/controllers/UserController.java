package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registration(@ModelAttribute("user") User user, @RequestParam(name = "loginError", required = false) boolean loginError, Model model) {
        if (loginError){
            model.addAttribute("loginError", true);
        }
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.register(user);
        } catch (DBException e) {
            return "redirect:/user/register?loginError=true";
        }

        return "redirect:/login";
    }
}
