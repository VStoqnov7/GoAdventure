package com.example.GoAdventure.web;

import com.example.GoAdventure.model.dtos.UserRegisterDTO;
import com.example.GoAdventure.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/login-register")
public class LoginRegisterController {
    private final UserService userService;

    public LoginRegisterController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute(name = "userRegisterDTO")
    public UserRegisterDTO userRegisterDTO() {
        return new UserRegisterDTO();
    }

    @GetMapping()
    public ModelAndView login(ModelAndView model) {
        model.setViewName("sign-up-sign-in");
        return model;
    }

    @PostMapping("/login-error")
    public ModelAndView onLoginFailure(ModelAndView model) {
        model.addObject("bad_credentials", true);
        model.setViewName("sign-up-sign-in");
        return model;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(ModelAndView model,
                                                @Valid UserRegisterDTO userRegisterDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.setViewName("sign-up-sign-in");
            model.addObject("hasErrors", true);
            return model;
        }
        this.userService.saveUser(userRegisterDTO);
        model.setViewName("redirect:/");
        return model;
    }

    @GetMapping("/confirm-account")
    public ModelAndView confirmUserAccount(@RequestParam("token") String token, ModelAndView model) {
        boolean isConfirmed = userService.confirmUserAccount(token);

        if (isConfirmed) {
            model.addObject("token", token);
            model.setViewName("redirect:/login-register");
        } else {
            model.setViewName("redirect:/login-register?error=invalid-token");
        }

        return model;
    }

    @GetMapping("/verification-page")
    public ModelAndView verificationPage(@RequestParam("token") String token, ModelAndView model, HttpServletRequest request) {
        String confirmationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                "/login-register/confirm-account?token=" + token;
        model.addObject("confirmationUrl", confirmationUrl);
        model.addObject("token", token);
        model.setViewName("verification-page");
        return model;
    }
}
