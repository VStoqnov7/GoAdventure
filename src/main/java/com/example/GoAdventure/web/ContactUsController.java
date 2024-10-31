package com.example.GoAdventure.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contact-us")
public class ContactUsController {

    @GetMapping
    public ModelAndView showGuestPage(ModelAndView model){
        model.setViewName("contact-us");
        return model;
    }
}
