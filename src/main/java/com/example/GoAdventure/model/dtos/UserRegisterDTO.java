package com.example.GoAdventure.model.dtos;

import com.example.GoAdventure.validation.uniqueEmail.UniqueEmail;
import com.example.GoAdventure.validation.uniqueUsername.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserRegisterDTO {

    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters!")
    @UniqueUsername(message = "Username already exists!")
    private String username;

    @Size(min = 5, max = 20, message = "Password length must be between 5 and 20 characters!")
    private String password;

    @NotBlank(message = "Email is required!")
    @Email(message = "Please provide a valid email address!")
    @UniqueEmail(message = "Email already exists!")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits!")
    private String phone;
}
