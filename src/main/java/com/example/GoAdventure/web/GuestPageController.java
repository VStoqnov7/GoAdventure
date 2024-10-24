package com.example.GoAdventure.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class GuestPageController {

    @GetMapping
    public ModelAndView showGuestPage(ModelAndView model){
        model.setViewName("guest-page");
        return model;
    }
}
