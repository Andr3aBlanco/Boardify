package com.boardify.boardify.controller;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.repository.UserRepository;
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


@Controller
public class AuthController {

    private UserService userService;
    private UserRepository userRepository;


    public AuthController(UserService userService, UserRepository userRepository) {

        this.userService = userService;
        this.userRepository = userRepository;

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
            result.rejectValue("email", null, "There is already  an account registered with that email");
        }
        if (user.getPassword().length() < 8) {
            result.rejectValue("password", null, "Password should be at least 8 characters long");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }



    @GetMapping("/your-profile")
    public String viewYourProfile(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String userEmail = userDetails.getUsername();
        UserDto userDto = userService.convertEntityToDto(userService.findByEmail(userEmail));
        model.addAttribute("loggedInUser", userDto);
        return "logged-in-profile";
    }

    @PostMapping("/edit-profile/{email}")
    public String editYourProfile(@PathVariable("email") String email, @ModelAttribute("loggedInUser") UserDto user) {
        User currentUser = userService.findByEmail(email);
        if (currentUser != null) {
            System.out.println(user.getState());
            userService.editLoggedInUser(email, user);
        }
        System.out.println("Step 2");
        return "redirect:/your-profile";
    }




}
