package com.example.GoAdventure.web;

import com.example.GoAdventure.model.dtos.UserRegisterDTO;
import com.example.GoAdventure.model.entity.User;
import com.example.GoAdventure.model.entity.UserRole;
import com.example.GoAdventure.model.entity.VerificationToken;
import com.example.GoAdventure.model.enums.Role;
import com.example.GoAdventure.repository.UserRepository;
import com.example.GoAdventure.repository.UserRoleRepository;
import com.example.GoAdventure.repository.VerificationTokenRepository;
import com.example.GoAdventure.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login-register"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up-sign-in"));
    }

    @Test
    void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login-register/login-error")
                        .with(csrf()))
                .andExpect(view().name("sign-up-sign-in"))
                .andExpect(model().attribute("bad_credentials", true));
    }


    @Test
    void testRegisterUser_ValidInput() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser2");
        userRegisterDTO.setPhone("1234567890");
        userRegisterDTO.setPassword("TestPass123");
        userRegisterDTO.setEmail("testuser@example.com");

        assertTrue(userService.findByEmail("testuser@example.com").isEmpty());

        mockMvc.perform(post("/login-register/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO)
                        .with(csrf()))
                .andExpect(redirectedUrl("/"));

        assertNotNull(userService.findByEmail("testuser@example.com"));
    }

    @Test
    @DirtiesContext
    void testRegisterUser_InvalidInput() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("");
        userRegisterDTO.setPhone("");
        userRegisterDTO.setPassword("");
        userRegisterDTO.setEmail("");

        mockMvc.perform(post("/login-register/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up-sign-in"))
                .andExpect(model().attributeExists("hasErrors"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDTO", "username"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDTO", "phone"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDTO", "password"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDTO", "email"));
    }

    @Test
    void testConfirmUserAccount_Success() throws Exception {
        String token = "validToken";

        UserRole userRole = new UserRole().setName(Role.USER);
        userRoleRepository.save(userRole);

        User user = new User()
                .setUsername("testUser")
                .setPassword("1122")
                .setEmail("example@mail.bg")
                .setPhone("1234567")
                .setBanned(false)
                .setEnabled(false)
                .setRoles(Set.of(userRole));
        this.userRepository.save(user);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);


        mockMvc.perform(get("/login-register/confirm-account")
                        .param("token", token))
                .andExpect(status().is3xxRedirection());

        User updatedUser = userRepository.findByEmail("example@mail.bg").orElseThrow();
        assertTrue(updatedUser.isEnabled());
    }

    @Test
    void testConfirmUserAccount_Failure() throws Exception {
        String token = "invalidToken";

        mockMvc.perform(get("/login-register/confirm-account")
                        .param("token", token))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login-register?error=invalid-token"));
    }

    @Test
    void testVerificationPage() throws Exception {
        String token = "validToken";

        mockMvc.perform(get("/login-register/verification-page")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("verification-page"))
                .andExpect(model().attributeExists("confirmationUrl"))
                .andExpect(model().attributeExists("token"));
    }
}