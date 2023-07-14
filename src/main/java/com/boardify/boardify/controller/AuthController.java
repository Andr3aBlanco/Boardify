package com.boardify.boardify.controller;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        String[] accountStatuses = {"Okay", "Banned", "Locked"};
        model.addAttribute("accStatuses", accountStatuses);
        UserDto tempUser = new UserDto();
        model.addAttribute("tempUser", tempUser);
        return "users";
    }

    @PostMapping("/users/edit/{email}")
    public String changeUserAccountStatus(@PathVariable("email") String email, @ModelAttribute("tempUser") UserDto tempUser){
        userService.changeAccountStatus(email, tempUser.getAccountStatus());

        return "redirect:/users";
    }

    @GetMapping("/your-profile")
    public String viewYourProfile(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String userEmail = userDetails.getUsername();
        UserDto userDto = userService.convertEntityToDto(userService.findByEmail(userEmail));
        model.addAttribute("currentUser", userDto);
        return "logged-in-profile";
    }

}
