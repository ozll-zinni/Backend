package com.example.traveler.repository;

import com.example.traveler.model.entity.DayCourse;
import com.example.traveler.model.entity.Travel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DayCourseRepository extends CrudRepository<DayCourse, Integer> {
    public DayCourse findBydcId(int id);
    public List<DayCourse> findAllByTravel(Travel travel);
    public DayCourse findByTravelAndNumOfDay(Travel travel, int numOfDay);
}
