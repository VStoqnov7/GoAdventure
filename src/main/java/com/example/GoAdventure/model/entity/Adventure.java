package com.example.GoAdventure.model.entity;

import com.example.GoAdventure.model.enums.AdventureType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "adventure")
public class Adventure extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @Column(name = "photo_url")
    private List<String> photoUrls;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    private Location location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdventureType type;
}
