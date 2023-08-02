package com.example.traveler.repository;

import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.model.entity.Travel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends CrudRepository<ChecklistEntity, Integer> {
    public ChecklistEntity findByTravel(Travel travel);
    public ChecklistEntity findBycIdAndState(int id, int state);

    public List<ChecklistEntity> findAllByTravel(Travel travel);
}
