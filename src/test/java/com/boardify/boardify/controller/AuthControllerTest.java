package com.boardify.boardify.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");
    }

    @Test
    public void testLoginForm() {
        String viewName = authController.loginForm();
        assertEquals("login", viewName);
    }

    @Test
    public void testShowRegistrationForm() {
        String viewName = authController.showRegistrationForm(model);
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any(UserDto.class));
    }

    @Test
    public void testRegistration_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String viewName = authController.registration(userDto, bindingResult, model);
        assertEquals("redirect:/register?success", viewName);
        verify(userService).saveUser(userDto);
    }

    @Test
    public void testRegistration_EmailExists() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.findByEmail(userDto.getEmail())).thenReturn(new User());
        String viewName = authController.registration(userDto, bindingResult, model);
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any(UserDto.class));
    }

    @Test
    public void testRegistration_InvalidPassword() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.findByEmail(userDto.getEmail())).thenReturn(null);
        String viewName = authController.registration(userDto, bindingResult, model);
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any(UserDto.class));
    }

    // Add more test cases for viewYourProfile and editYourProfile methods
}
