package com.example.GoAdventure.repository;


import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.enums.AdventureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdventureRepository extends JpaRepository<Adventure,Long> {

    List<Adventure> findByLocationNameAndType(String locationName, AdventureType adventureType);
}
