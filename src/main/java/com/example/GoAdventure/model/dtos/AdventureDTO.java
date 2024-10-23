package com.example.GoAdventure.model.dtos;

import com.example.GoAdventure.model.enums.AdventureType;
import com.example.GoAdventure.validation.validPhotos.ValidPhotos;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class AdventureDTO {

    @NotBlank(message = "Name is required!")
    private String name;

    @NotNull(message = "Price cannot be empty!")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0!")
    private BigDecimal price;

    @NotBlank(message = "Location name must not be empty!")
    @Size(max = 20, message = "Location name cannot be longer than 20 characters!")
    private String locationName;

    @NotBlank(message = "Coordinates must not be empty!")
    private String locationCoordinates;

    @NotNull(message = "Adventure type is required!")
    private AdventureType type;

    @Min(value = 1, message = "Duration must be at least 1 hour!")
    private int duration;

    @NotBlank(message = "Description is required!")
    @Size(max = 100, message = "Description cannot be longer than 100 characters!")
    private String description;

    @ValidPhotos
    private List<MultipartFile> photoUrls;
}
