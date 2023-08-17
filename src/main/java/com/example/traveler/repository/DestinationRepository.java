package com.example.traveler.repository;

import com.example.traveler.model.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Integer> {
    public Destination findBydId(int id);
}
