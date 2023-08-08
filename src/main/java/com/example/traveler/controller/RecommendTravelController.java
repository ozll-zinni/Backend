package com.example.traveler.controller;

import com.example.traveler.model.dto.RecommendTravelRequest;
import com.example.traveler.model.entity.RecommendTravel;
import com.example.traveler.service.RecommendTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendTravelController {

    @Autowired
    private RecommendTravelService recommendTravelService;

    @GetMapping
    public ResponseEntity<List<RecommendTravel>> recommendTravel(@RequestBody RecommendTravelRequest request) {
        List<RecommendTravel> matchingTravels = recommendTravelService.getMatchingTravels(request);

        if (matchingTravels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(matchingTravels, HttpStatus.OK);
    }

}