package com.example.GoAdventure.init;

import jakarta.annotation.PostConstruct;

public interface DataBaseInitService {
    @PostConstruct
    void init();
}