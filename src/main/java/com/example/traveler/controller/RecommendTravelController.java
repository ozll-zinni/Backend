package com.example.traveler.controller;

import com.example.traveler.model.dto.RecommendTravelRequest;
import com.example.traveler.model.dto.MainrecoResponse;
import com.example.traveler.model.dto.TravelResponse;
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
@CrossOrigin(origins = {"http://localhost:3000", "https://traveler-smoky.vercel.app"})
public class RecommendTravelController {

    @Autowired
    private RecommendTravelService recommendTravelService;

    @PostMapping
    public ResponseEntity<TravelResponse> recommendTravel(@RequestHeader("Authorization") String accessToken, @RequestBody RecommendTravelRequest request) {
        //List<RecommendTravel> matchingTravels = recommendTravelService.getMatchingTravels(accessToken, request);
        TravelResponse travelResponse;
        try {
            travelResponse = recommendTravelService.getMatchingTravels2(accessToken, request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(travelResponse, HttpStatus.OK);
    }

    @GetMapping("/regieon")
    public ResponseEntity<List<Destination>> getAllRegion(){
        List<Destination> allrigeon = recommendTravelService.getAllregieon();
        return ResponseEntity.ok(allrigeon);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MainrecoResponse>> getList() {
        List<MainrecoResponse> allRT = recommendTravelService.getList();
        return ResponseEntity.ok(allRT);
    }


}