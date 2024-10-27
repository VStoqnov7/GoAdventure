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
@Table(name = "adventures")
public class Adventure extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    private Location location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdventureType type;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

//  Max 6 pictures
    @ElementCollection
    @Column(name = "photo_url")
    private List<String> photoUrls;
}
