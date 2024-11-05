package com.example.GoAdventure.service;

import com.example.GoAdventure.config.AdminConfig;
import com.example.GoAdventure.model.dtos.UserRegisterDTO;
import com.example.GoAdventure.model.entity.User;
import com.example.GoAdventure.model.entity.UserRole;
import com.example.GoAdventure.model.entity.VerificationToken;
import com.example.GoAdventure.model.enums.Role;
import com.example.GoAdventure.repository.UserRepository;
import com.example.GoAdventure.repository.VerificationTokenRepository;
import com.example.GoAdventure.service.interfaces.UserRoleService;
import com.example.GoAdventure.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final AdminConfig adminConfig;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final VerificationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    @Value("${app.mysql.host}")
    private String mysqlHost;

    @Value("${app.mysql.port}")
    private String mysqlPort;

    public UserServiceImpl(AdminConfig adminConfig, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRepository userRepository, UserRoleService userRoleService, VerificationTokenRepository tokenRepository, JavaMailSender mailSender) {
        this.adminConfig = adminConfig;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
    }

    @Override
    public void saveUser(UserRegisterDTO userRegisterDTO) {
        User user = this.modelMapper.map(userRegisterDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.getRoles().add(this.userRoleService.findByRole(Role.USER));
        user.setEnabled(false);
        this.userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken()
                .setToken(token)
                .setUser(user)
                .setExpiryDate(LocalDateTime.now().plusDays(1));
        tokenRepository.save(verificationToken);

        sendVerificationEmail(user.getEmail(), token);
    }


    private void sendVerificationEmail(String email, String token) {
        String verificationUrl = "http://" + mysqlHost + ":" + mysqlPort + "/login-register/verification-page?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirm your registration");
        message.setText("Click the link to confirm your registration: " + verificationUrl);
        mailSender.send(message);
    }

    @Override
    public void dbInitAdmin() {
        if (this.userRepository.count() == 0) {
            Set<UserRole> roles = new HashSet<>();
            roles.add(this.userRoleService.findByRole(Role.USER));
            roles.add(this.userRoleService.findByRole(Role.ADMIN));

            User user = new User()
                    .setUsername(adminConfig.getUsername())
                    .setPassword(passwordEncoder.encode(adminConfig.getPassword()))
                    .setEmail(adminConfig.getEmail())
                    .setPhone(adminConfig.getPhone())
                    .setRoles(roles)
                    .setEnabled(true);
            this.userRepository.save(user);
        }
    }

    @Override
    public boolean confirmUserAccount(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);

        if (verificationToken != null && verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
