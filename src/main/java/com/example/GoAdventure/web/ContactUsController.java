package com.example.GoAdventure.web;

import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.util.ModelAttributeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contact-us")
public class ContactUsController {

    private final AdventureService adventureService;

    public ContactUsController(AdventureService adventureService, JavaMailSender mailSender) {
        this.adventureService = adventureService;
    }

    @GetMapping
    public ModelAndView showGuestPage(ModelAndView model) {
        ModelAttributeUtil.addInitialFourAdventures(adventureService, model);
        model.setViewName("contact-us");
        return model;
    }

    @PostMapping("/send-message")
    public ModelAndView sendMessage(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            ModelAndView model
    ) {
        if (name.isBlank() || email.isBlank() || subject.isBlank() || message.isBlank()) {
            model.addObject("errorMessage", "All fields are required!");
            ModelAttributeUtil.addInitialFourAdventures(adventureService, model);
            model.setViewName("contact-us");
            return model;
        }

        this.adventureService.sentMessage(name,email,subject,message);

        model.addObject("successMessage", "Your message has been sent successfully!");
        ModelAttributeUtil.addInitialFourAdventures(adventureService, model);
        model.setViewName("contact-us");
        return model;
    }
}
