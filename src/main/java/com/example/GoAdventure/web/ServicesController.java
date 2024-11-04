package com.example.GoAdventure.web;

import com.example.GoAdventure.model.dtos.AdventureDTO;
import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.util.ModelAttributeUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/services")
public class ServicesController {

    private final AdventureService adventureService;

    public ServicesController(AdventureService adventureService) {
        this.adventureService = adventureService;
    }

    @ModelAttribute(name = "adventureDTO")
    public AdventureDTO adventureDTO() {
        return new AdventureDTO();
    }

    @GetMapping
    public ModelAndView showServices(ModelAndView model){
        ModelAttributeUtil.addEnumsToService(model);
        model.setViewName("services");
        return model;
    }

    @PostMapping("/createAdventure")
    public ModelAndView createAdventure(ModelAndView model,
                                        @Valid AdventureDTO adventureDTO,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.adventureDTO", bindingResult);
            redirectAttributes.addFlashAttribute("adventureDTO", adventureDTO);
            model.setViewName("redirect:/services");
            return model;
        }

        this.adventureService.saveAdventure(adventureDTO);
        model.setViewName("redirect:/services");
        return model;
    }
}
