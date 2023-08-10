package com.example.traveler.repository;

import com.example.traveler.model.entity.RecommendTravel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendTravelRepository extends JpaRepository<RecommendTravel, Integer> {

    List<RecommendTravel> findByCode1(int code1);


}
