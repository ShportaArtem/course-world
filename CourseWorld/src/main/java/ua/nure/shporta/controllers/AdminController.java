package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.shporta.exception.DBException;
import ua.nure.shporta.helper.Helper;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private Helper helper;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listModerators(@RequestParam("page") Optional<Integer> page, Model model) {
        Page<User> moderatorsPage = userService.findAllModerators(page);
        model.addAttribute("moderatorsPage", moderatorsPage);

        if (moderatorsPage.getTotalPages() > 0) {
            model.addAttribute("pageNumbers", helper.getPages(moderatorsPage.getTotalPages()));
        }
        return "moderators";
    }

    @GetMapping("/moderator/add")
    public String getCreateForm(@RequestParam(name = "loginError", required = false) boolean loginError, Model model) {
        model.addAttribute("moderator", new User());
        if (loginError) {
            model.addAttribute("loginError", true);
        }
        return "addModerator";
    }

    @PostMapping("/moderator/add")
    public String addModerator(@ModelAttribute User moderator, Model model) {
        try {
            userService.registerModerator(moderator);
        } catch (
                DBException e) {
            return "redirect:/admin/moderator/add?loginError=true";
        }
        return "redirect:/admin";
    }

    @PostMapping("/moderator/{id}/delete")
    public String deleteModerator(@PathVariable Integer id, Model model) {
        userService.deleteUser(id);

        return "redirect:/admin";
    }
}
