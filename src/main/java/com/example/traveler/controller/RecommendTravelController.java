package com.example.traveler.controller;

import com.example.traveler.model.dto.RecommendTravelRequest;
import com.example.traveler.model.entity.Destination;
import com.example.traveler.model.entity.RecommendTravel;
import com.example.traveler.service.RecommendTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommend")
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendTravelController {

    @Autowired
    private RecommendTravelService recommendTravelService;

    @PostMapping
    public ResponseEntity<List<RecommendTravel>> recommendTravel(@RequestHeader("Authorization") String accessToken, @RequestBody RecommendTravelRequest request) {
        List<RecommendTravel> matchingTravels = recommendTravelService.getMatchingTravels(accessToken, request);

        if (matchingTravels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(matchingTravels, HttpStatus.OK);
    }

    @GetMapping("/regieon")
    public ResponseEntity<List<Destination>> getAllRegion(){
        List<Destination> allrigeon = recommendTravelService.getAllregieon();
        return ResponseEntity.ok(allrigeon);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RecommendTravel>> getAllRT() {
        List<RecommendTravel> allRT = recommendTravelService.getAllrecommendTravel();
        return ResponseEntity.ok(allRT);
    }

}