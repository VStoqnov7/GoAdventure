package com.example.GoAdventure.repository;


import com.example.GoAdventure.model.entity.Adventure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdventureRepository extends JpaRepository<Adventure,Long> {

}
