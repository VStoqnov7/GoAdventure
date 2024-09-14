package com.example.GoAdventure.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class LoginController {

    @GetMapping("/login")
    public ModelAndView login(ModelAndView model) {
        model.setViewName("sign-up-sign-in");
        return model;
    }

    @PostMapping("/login-error")
    public ModelAndView onLoginFailure(ModelAndView modelAndView) {
        modelAndView.addObject("bad_credentials", true);
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
