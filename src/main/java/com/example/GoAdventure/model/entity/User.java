package com.example.GoAdventure.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;

    @Column(name = "is_banned")
    private boolean isBanned;

    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles;

    public User() {
        this.roles = new LinkedHashSet<>();
    }
}
