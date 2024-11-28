package com.example.GoAdventure.repository;


import com.example.GoAdventure.model.entity.Adventure;
import com.example.GoAdventure.model.enums.AdventureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface AdventureRepository extends JpaRepository<Adventure,Long> {

    @Query("SELECT a FROM Adventure a WHERE a.id BETWEEN 1 AND 3")
    List<Adventure> findInitialThreeAdventures();

    @Query("SELECT a FROM Adventure a WHERE a.id BETWEEN 1 AND 4")
    List<Adventure> findInitialFourAdventures();

    @Query("SELECT a FROM Adventure a WHERE a.id BETWEEN 4 AND 6")
    List<Adventure> findFollowingThreeAdventures();

    @Query("SELECT a FROM Adventure a WHERE a.id BETWEEN 6 AND 16")
    List<Adventure> findExtendedTenAdventures();

    List<Adventure> findByLocationNameAndType(String locationName, AdventureType adventureType);

}
