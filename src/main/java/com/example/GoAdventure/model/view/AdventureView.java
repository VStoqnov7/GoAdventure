package com.example.GoAdventure.model.view;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class AdventureView {

    private Long id;
    private String name;
    private BigDecimal price;
    private String locationName;
    private String locationCoordinates;
    private String type;
    private Integer duration;
    private String description;
    private List<String> photoUrls;
}
