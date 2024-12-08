package com.example.GoAdventure.web;

import com.example.GoAdventure.model.entity.*;
import com.example.GoAdventure.model.enums.AdventureType;
import com.example.GoAdventure.model.enums.Role;
import com.example.GoAdventure.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ServicesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdventureRepository adventureRepository;

    @Autowired
    private PendingBookingRepository pendingBookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void testShowServices() throws Exception {
        mockMvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andExpect(view().name("services"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attributeExists("pendingBookings"));
    }


    @Test
    @Transactional
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void testCreateAdventure_ValidData() throws Exception {
        MockMultipartFile picture1 = getMockMultipartFile();

        mockMvc.perform(multipart("/services/createAdventure")
                        .file(picture1)  // Добавяме снимката към заявката
                        .param("name", "New Adventure")
                        .param("price", "150")
                        .param("locationName", "Sofia")
                        .param("locationCoordinates", "42.698334,23.319941")
                        .param("type", "HIKING")
                        .param("duration", "5")
                        .param("description", "Test description")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        List<Adventure> adventures = adventureRepository.findAll();
        assertThat(adventures).hasSize(1);
        Adventure newAdventure = adventures.get(0);
        assertThat(newAdventure.getName()).isEqualTo("New Adventure");
        assertThat(newAdventure.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(newAdventure.getType()).isEqualTo(AdventureType.HIKING);
        assertThat(newAdventure.getDuration()).isEqualTo(5);
        assertThat(newAdventure.getDescription()).isEqualTo("Test description");
        assertThat(newAdventure.getPhotoUrls()).isNotEmpty();
    }

    @Test
    @Transactional
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void testCreateAdventure_InvalidData() throws Exception {
        MockMultipartFile picture1 = getMockMultipartFile();

        mockMvc.perform(multipart("/services/createAdventure")
                        .file(picture1)
                        .param("name", "")
                        .param("price", "-150")
                        .param("locationName", "")
                        .param("locationCoordinates", "invalidCoordinates")
                        .param("type", "INVALID_TYPE")
                        .param("duration", "0")
                        .param("description", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        List<Adventure> adventures = adventureRepository.findAll();
        assertThat(adventures).hasSize(0);
    }

    public MockMultipartFile getMockMultipartFile() throws IOException {
        String imagePath = "src/test/resources/static/images/TEST.jpeg";
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        MockMultipartFile picture1 = new MockMultipartFile(
                "photoUrls",
                "plane.jpg",
                "image/jpeg",
                imageBytes);
        return picture1;
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void testApproveBooking_Success() throws Exception {
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

        PendingBooking pendingBooking = new PendingBooking()
                .setUser(user)
                .setAdventure(adventure)
                .setBookingDate(LocalDateTime.now())
                .setRegisteredOn(LocalDateTime.now());

        this.pendingBookingRepository.save(pendingBooking);

        assertThat(bookingRepository.findAll()).hasSize(0);

        Long validBookingId = pendingBooking.getId();

        mockMvc.perform(post("/services/approveBooking/{pendingBookingId}", validBookingId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        assertThat(bookingRepository.findAll()).hasSize(1);
        assertThat(pendingBookingRepository.findAll()).hasSize(0);
    }


    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void testRejectBooking() throws Exception {
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

        PendingBooking pendingBooking = new PendingBooking()
                .setUser(user)
                .setAdventure(adventure)
                .setBookingDate(LocalDateTime.now())
                .setRegisteredOn(LocalDateTime.now());

        this.pendingBookingRepository.save(pendingBooking);

        assertThat(bookingRepository.findAll()).hasSize(0);

        Long validBookingId = pendingBooking.getId();

        mockMvc.perform(post("/services/rejectBooking/{pendingBookingId}", validBookingId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        assertThat(bookingRepository.findAll()).hasSize(0);
        assertThat(pendingBookingRepository.findAll()).hasSize(0);
    }
}