package com.example.GoAdventure.web;

import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.entity.Location;
import com.example.GoAdventure.model.entity.User;
import com.example.GoAdventure.model.entity.UserRole;
import com.example.GoAdventure.model.enums.AdventureType;
import com.example.GoAdventure.model.enums.Role;
import com.example.GoAdventure.repository.AdventureRepository;
import com.example.GoAdventure.repository.PendingBookingRepository;
import com.example.GoAdventure.repository.UserRepository;
import com.example.GoAdventure.repository.UserRoleRepository;
import com.example.GoAdventure.service.interfaces.AdventureService;
import com.example.GoAdventure.service.interfaces.PendingBookingService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdventureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdventureRepository adventureRepository;

    @Autowired
    private AdventureService adventureService;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @BeforeEach
    void setUp() {
        adventureRepository.deleteAll();
        userRepository.deleteAll();

        Adventure adventure = new Adventure()
                .setName("Mountain Trekking")
                .setPrice(BigDecimal.valueOf(120.00))
                .setLocation(new Location().setName("Rila")
                        .setCoordinates("42.142532,23.542328"))
                .setType(AdventureType.HIKING)
                .setDuration(4)
                .setDescription("A breathtaking mountain trekking experience.")
                .setPhotoUrls(List.of("https://example.com/photo1.jpg", "https://example.com/photo2.jpg"));

        adventureRepository.save(adventure);

    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testShowAdventures() throws Exception {
        mockMvc.perform(get("/adventures"))
                .andExpect(status().isOk())
                .andExpect(view().name("adventures"))
                .andExpect(model().attributeExists("allAdventureList"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testSearchAdventures_ValidInput() throws Exception {
        mockMvc.perform(post("/adventures/search")
                        .param("cityName", "Rila")
                        .param("adventureType", "Hiking")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("adventures"))
                .andExpect(model().attributeExists("allAdventureList"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testSearchAdventures_InvalidInput() throws Exception {
        String invalidLocation = "";
        String invalidAdventureType = "";

        mockMvc.perform(post("/adventures/search")
                        .param("cityName", invalidLocation)
                        .param("adventureType", invalidAdventureType)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("adventures"))
                .andExpect(model().attributeExists("allAdventureList"))
                .andExpect(model().attributeExists("cityNameError"))
                .andExpect(model().attributeExists("adventureTypeError"))
                .andExpect(model().attribute("cityNameError", true))
                .andExpect(model().attribute("adventureTypeError", true));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testShowAdventureProfile() throws Exception {
        Adventure adventure = adventureRepository.findAll().get(0);

        mockMvc.perform(get("/adventures/" + adventure.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("adventure-profile"))
                .andExpect(model().attributeExists("adventure"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testCreateReservation_InvalidDate() throws Exception {
        Adventure adventure = adventureRepository.findAll().get(0);

        mockMvc.perform(post("/adventures/createReservation/" + adventure.getId())
                        .param("reservationDate", "2020-12-01T10:00")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("adventure-profile"))
                .andExpect(model().attributeExists("invalidDate"))
                .andExpect(model().attribute("invalidDate", true))
                .andExpect(model().attributeExists("adventure"));
    }


    @Test
    @Transactional
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testCreateReservation_InvalidDate1() throws Exception {
        Adventure adventure = adventureRepository.findAll().get(0);

        mockMvc.perform(post("/adventures/createReservation/" + adventure.getId())
                        .param("reservationDate", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("adventure-profile"))
                .andExpect(model().attributeExists("invalidDate"))
                .andExpect(model().attribute("invalidDate", true))
                .andExpect(model().attributeExists("adventure"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "example@mail.bg", roles = {"USER"})
    void testCreateReservation_ValidDate() throws Exception {
        Adventure adventure = adventureRepository.findAll().get(0);

        UserRole userRole = new UserRole().setName(Role.USER);
        userRoleRepository.save(userRole);

        User user = new User()
                .setUsername("testUser")
                .setPassword("1122")
                .setEmail("example@mail.bg")
                .setPhone("1234567")
                .setBanned(false)
                .setEnabled(true)
                .setRoles(Set.of(userRole));
        this.userRepository.save(user);

        mockMvc.perform(post("/adventures/createReservation/" + adventure.getId())
                        .param("reservationDate", "2025-12-01T10:00")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("adventure-profile"))
                .andExpect(model().attributeExists("validDate"))
                .andExpect(model().attribute("validDate", true));
    }

    @Test
    @Transactional
    @WithMockUser(username = "adminUser", roles = {"ADMIN","USER"})
    void testDeleteAdventure() throws Exception {
        Adventure adventure = adventureRepository.findAll().get(0);

        mockMvc.perform(post("/adventures/delete/" + adventure.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/adventures"));

        assertThat(adventureRepository.findById(adventure.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN","USER"})
    void testAddEnumsToAdventures() throws Exception {
        mockMvc.perform(get("/adventures"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("range1"))
                .andExpect(model().attributeExists("range2"))
                .andExpect(model().attributeExists("range3"))
                .andExpect(model().attributeExists("range4"))
                .andExpect(model().attributeExists("range5"))
                .andExpect(model().attributeExists("range6"))
                .andExpect(model().attributeExists("range7"));
    }

    @AfterEach
    void cleanUp() {
        adventureRepository.deleteAll();
        userRepository.deleteAll();
    }
}