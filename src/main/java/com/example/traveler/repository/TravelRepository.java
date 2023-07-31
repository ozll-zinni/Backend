package com.example.traveler.repository;

import com.example.traveler.model.entity.Travel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TravelRepository extends CrudRepository<Travel, Integer> {
    public Travel findBytIdAndState(int id, int state);
    //public List<Travel> findAllByUser()
    public List<Travel> findAllByState(int state);

    //public List<Travel> findAllByUser(User user);
}
