package com.example.GoAdventure.web;

import com.example.GoAdventure.model.dtos.UserRegisterDTO;
import com.example.GoAdventure.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        model.setViewName("login");
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
}
