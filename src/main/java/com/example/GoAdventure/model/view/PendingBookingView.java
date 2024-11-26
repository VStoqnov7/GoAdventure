package com.example.GoAdventure.model.view;

import com.example.GoAdventure.model.entity.Adventure;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PendingBookingView {

    private Long id;
    private String userUsername;
    private String userEmail;
    private String userPhone;
    private AdventureView adventureView;
}
