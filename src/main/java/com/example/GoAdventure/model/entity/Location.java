package com.example.GoAdventure.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "locations")
public class Location extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String coordinates;
}
