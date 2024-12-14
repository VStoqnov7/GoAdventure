package com.example.GoAdventure.web;

import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.util.ModelAttributeUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testUser", roles = {"USER"})
class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdventureService adventureService;

    @Autowired
    private HomeController homeController;


    @Test
    public void testHomePage() throws Exception {

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("initialAdventures"))
                .andExpect(model().attributeExists("followingAdventures"))
                .andExpect(model().attributeExists("extendedAdventures"));
    }

    @Test
    public void testAddInitialThreeAdventures() {
        ModelAndView model = new ModelAndView();

        ModelAttributeUtil.addInitialThreeAdventures(adventureService, model);

        assertNotNull(model.getModel().get("initialAdventures"));
    }

    @Test
    public void testAddFollowingThreeAdventures() {
        ModelAndView model = new ModelAndView();

        ModelAttributeUtil.addFollowingThreeAdventures(adventureService, model);

        assertNotNull(model.getModel().get("followingAdventures"));
    }

    @Test
    public void testAddExtendedTenAdventureList() {
        ModelAndView model = new ModelAndView();

        ModelAttributeUtil.addExtendedTenAdventureList(adventureService, model);

        assertNotNull(model.getModel().get("extendedAdventures"));
    }

}