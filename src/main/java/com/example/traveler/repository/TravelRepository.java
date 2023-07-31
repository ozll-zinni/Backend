package com.example.traveler.repository;

import com.example.traveler.model.entity.Travel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TravelRepository extends CrudRepository<Travel, Integer> {
    public Travel findById(int id);
    //public List<Travel> findAllByUser()
    public List<Travel> findAll();

    //public List<Travel> findAllByUser(User user);
}
