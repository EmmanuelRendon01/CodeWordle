package com.codewordle.codewordle.controller;

import com.codewordle.codewordle.dto.UserCreateRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller dedicated to handling requests for server-side rendered views (JSP).
 * Note the use of @Controller instead of @RestController.
 */
@Controller
public class ViewController {

    /**
     * Displays the main dashboard page after a user successfully logs in.
     * This is a protected route.
     * @param model The model to pass data to the view.
     * @return The name of the dashboard view.
     */
    @GetMapping("/dashboard")
    public String showDashboard() {

        return "dashboard";
    }

    /**
     * Displays the login page.
     * @return The name of the login view ("login"), which will be resolved to /WEB-INF/jsp/login.jsp.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        // This simply returns the name of the JSP file.
        return "login";
    }

    /**
     * Displays the user registration page.
     * It also adds an empty UserCreateRequest object to the model. This is useful
     * for Spring's form binding in more advanced scenarios.
     *
     * @param model The model to pass data to the view.
     * @return The name of the registration view ("register").
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userCreateRequest", new UserCreateRequest());
        return "register";
    }
}