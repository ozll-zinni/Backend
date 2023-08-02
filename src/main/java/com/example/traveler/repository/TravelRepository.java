package com.example.traveler.repository;

import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TravelRepository extends CrudRepository<Travel, Integer> {
    public Travel findBytIdAndState(int id, int state);
    //public List<Travel> findAllByUser()
    public List<Travel> findAllByState(int state);

    public List<Travel> findAllByUserAndState(User user, int state);
    public List<Travel> findAllByUserAndStateAndTimeStatus(User user, int state, int time_status);
}