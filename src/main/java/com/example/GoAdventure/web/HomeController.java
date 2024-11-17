package com.example.GoAdventure.web;

import com.example.GoAdventure.model.view.AdventureView;
import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.util.ModelAttributeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final AdventureService adventureService;

    public HomeController(AdventureService adventureService) {
        this.adventureService = adventureService;
    }

    @GetMapping
    public ModelAndView home(ModelAndView model){
        ModelAttributeUtil.addInitialThreeAdventures(adventureService,model);
        ModelAttributeUtil.addFollowingThreeAdventures(adventureService,model);
        ModelAttributeUtil.addExtendedTenAdventureList(adventureService,model);
        model.setViewName("home");
        return model;
    }
}
