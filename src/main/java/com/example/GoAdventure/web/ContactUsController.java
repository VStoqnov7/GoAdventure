package com.example.GoAdventure.web;

import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.util.ModelAttributeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contact-us")
public class ContactUsController {

    private final AdventureService adventureService;

    public ContactUsController(AdventureService adventureService) {
        this.adventureService = adventureService;
    }

    @GetMapping
    public ModelAndView showGuestPage(ModelAndView model){
        ModelAttributeUtil.addInitialFourAdventures(adventureService,model);
        model.setViewName("contact-us");
        return model;
    }
}
