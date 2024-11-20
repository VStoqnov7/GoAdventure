package com.example.GoAdventure.util;

import com.example.GoAdventure.model.enums.AdventureType;
import com.example.GoAdventure.model.view.AdventureView;
import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.service.interfaces.PendingBookingService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModelAttributeUtil {

    public static void addEnumsToService(ModelAndView model) {
        model.addObject("types", AdventureType.values());
    }

    public static void addEnumsToAdventures(ModelAndView model){
        AdventureType[] types = AdventureType.values();
        model.addObject("types", types);
        model.addObject("range1", IntStream.range(0, 6).boxed().collect(Collectors.toList()));
        model.addObject("range2", IntStream.range(6,12).boxed().collect(Collectors.toList()));
        model.addObject("range3", IntStream.range(12,18).boxed().collect(Collectors.toList()));
        model.addObject("range4", IntStream.range(18,24).boxed().collect(Collectors.toList()));
        model.addObject("range5", IntStream.range(24,30).boxed().collect(Collectors.toList()));
        model.addObject("range6", IntStream.range(30,36).boxed().collect(Collectors.toList()));
        model.addObject("range7", IntStream.range(36,39).boxed().collect(Collectors.toList()));
    }

    public static void addObjectsIfHasErrors(ModelAndView model, String locationName, String adventureType, Pageable pageable) {
        model.addObject("cityNameText", locationName);
        model.addObject("adventureTypeText", adventureType);
        model.addObject("allAdventureList", new PageImpl<>(Collections.emptyList(), pageable, 0));
    }

    public static void addAllPendingBookings(ModelAndView model, PendingBookingService pendingBookingService) {
        model.addObject("pendingBookings", pendingBookingService.findAll());
    }

    public static void addInitialThreeAdventures(AdventureService adventureService, ModelAndView model) {
        model.addObject("initialAdventures",adventureService.getInitialThreeAdventures());
    }

    public static void addFollowingThreeAdventures(AdventureService adventureService, ModelAndView model) {
        model.addObject("followingAdventures",adventureService.getFollowingThreeAdventures());
    }

    public static void addExtendedTenAdventureList(AdventureService adventureService, ModelAndView model) {
        model.addObject("extendedAdventures",adventureService.getExtendedTenAdventureList());
    }

    public static void addInitialFourAdventures(AdventureService adventureService, ModelAndView model) {
        model.addObject("initialFourAdventures",adventureService.getInitialFourAdventures());
    }
}
