package com.example.GoAdventure.web;

import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.util.ModelAttributeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContactUsControllerTest {


    @Autowired
    private AdventureService adventureService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShowGuestPage() throws Exception {

        mockMvc.perform(get("/contact-us"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact-us"))
                .andExpect(model().attributeExists("initialFourAdventures"));
    }

    @Test
    public void testAddInitialFourAdventures() {
        ModelAndView model = new ModelAndView();

        ModelAttributeUtil.addInitialFourAdventures(adventureService, model);

        assertNotNull(model.getModel().get("initialFourAdventures"));
    }

    @Test
    @DirtiesContext
    public void testShowGuestPageWhenNoAdventures() throws Exception {

        mockMvc.perform(get("/contact-us"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact-us"))
                .andExpect(model().attribute("initialFourAdventures", Collections.emptyList()));
    }
}