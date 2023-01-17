package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.UserServiceImpl;
import web.util.UserValidator;

import java.security.Principal;
import java.util.List;

@Controller
public class UserAndAdminController {

    private final UserValidator userValidator;

    private final UserServiceImpl userService;

    @Autowired
    public UserAndAdminController(UserValidator userValidator, UserServiceImpl userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping("/")
    public String main() {
        return "/main";
    }

    @GetMapping("/user")
    public String userInfo(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "/user";
    }

    @GetMapping("/admin")
    public String index(ModelMap model) {
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("users", allUsers);
        return "/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        return "/show";
    }

    @GetMapping("/new")
    public String NewUser(@ModelAttribute("user") User user) {
        return "/new";
    }

    @PostMapping("/")
    public String create(@ModelAttribute("user") User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/new";
        }
        userService.add(user);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Integer id) {
        User user = userService.findOne(id);
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") Integer id) {
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "redirect:/";
    }
}
