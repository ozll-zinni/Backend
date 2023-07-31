package com.example.traveler.repository;

import com.example.traveler.model.entity.Spot;
import org.springframework.data.repository.CrudRepository;

public interface SpotRepository extends CrudRepository<Spot, Integer> {
    public Spot findById(int id);

}
