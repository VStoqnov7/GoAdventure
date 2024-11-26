package com.example.GoAdventure.web;

import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.enums.AdventureType;
import com.example.GoAdventure.model.view.AdventureView;
import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.service.interfaces.PendingBookingService;
import com.example.GoAdventure.util.ModelAttributeUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/adventures")
public class AdventureController {

    private final AdventureService adventureService;
    private final PendingBookingService pendingBookingService;

    public AdventureController(AdventureService adventureService, PendingBookingService pendingBookingService) {
        this.adventureService = adventureService;
        this.pendingBookingService = pendingBookingService;
    }

    @GetMapping
    public ModelAndView showAdventures(@PageableDefault(sort = "id", size = 12) Pageable pageable, ModelAndView model) {
        List<AdventureView> adventures = adventureService.findAllAdventures();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), adventures.size());

        Page<AdventureView> allAdventures = new PageImpl<>(
                adventures.subList(start, end),
                pageable,
                adventures.size()
        );
        model.addObject("allAdventureList", allAdventures);
        ModelAttributeUtil.addEnumsToAdventures(model);
        model.setViewName("adventures");
        return model;
    }

    @PostMapping("/search")
    public ModelAndView searchAdventures(@PageableDefault(sort = "id", size = 12) Pageable pageable,
                                         @RequestParam("cityName") String locationName,
                                         @RequestParam("adventureType") String adventureType,
                                         ModelAndView model) {
        ModelAttributeUtil.addEnumsToAdventures(model);

        if (validateInputs(locationName, adventureType, model)) {
            ModelAttributeUtil.addObjectsIfHasErrors(model, locationName, adventureType, pageable);
        } else {
            List<AdventureView> adventures = adventureService.findAdventuresByLocationNameAndType(locationName,
                    AdventureType.valueOf(adventureType.toUpperCase().replace(" ", "_")));

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), adventures.size());

            Page<AdventureView> allAdventures = new PageImpl<>(
                    adventures.subList(start, end),
                    pageable,
                    adventures.size()
            );

            model.addObject("allAdventureList", allAdventures);
        }

        model.addObject("city", locationName);
        model.addObject("type", adventureType);
        model.setViewName("adventures");
        return model;
    }

    private boolean validateInputs(String locationName, String adventureType, ModelAndView model) {
        boolean hasErrors = false;

        if (locationName == null || locationName.trim().isEmpty()) {
            model.addObject("cityNameError", true);
            hasErrors = true;
        }
        if (adventureType == null || adventureType.trim().isEmpty()) {
            model.addObject("adventureTypeError", true);
            hasErrors = true;
        }

        return hasErrors;
    }

    @GetMapping("/{adventureId}")
    public ModelAndView showAdventureProfile(@PathVariable Long adventureId,
                                             ModelAndView model) {
        Adventure adventure = adventureService.findById(adventureId);
        model.addObject("adventure", adventureService.createAdventureProfileView(adventure));
        model.setViewName("adventure-profile");
        return model;
    }

    @PostMapping("/createReservation/{adventureId}")
    public ModelAndView createReservationForAdventure(@PathVariable Long adventureId,
                                                      @RequestParam("reservationDate") String reservationDate,
                                                      ModelAndView model,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        Adventure adventure = adventureService.findById(adventureId);

        if (reservationDate == null || reservationDate.isEmpty()) {
            model.addObject("invalidDate", true);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(reservationDate, formatter);

            if (dateTime.isBefore(LocalDateTime.now())) {
                model.addObject("invalidDate", true);
            } else {
                model.addObject("validDate", true);
                this.pendingBookingService.savePendingBooking(adventure,dateTime,userDetails);
            }
        }

        model.addObject("adventure", adventureService.createAdventureProfileView(adventure));

        model.setViewName("adventure-profile");
        return model;
    }

    @PostMapping("/delete/{adventureId}")
    public String deleteAdventure(@PathVariable Long adventureId) {
        adventureService.deleteAdventure(adventureId);
        return "redirect:/adventures";
    }
}
