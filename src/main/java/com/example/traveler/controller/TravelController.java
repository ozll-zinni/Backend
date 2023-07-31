package com.example.traveler.controller;

import com.example.traveler.model.dto.*;
import com.example.traveler.service.DayCourseService;
import com.example.traveler.service.SpotService;
import com.example.traveler.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travel")
public class TravelController {
    @Autowired
    private TravelService travelService;
    @Autowired
    private DayCourseService dayCourseService;
    @Autowired
    private SpotService spotService;
    @PostMapping("")
    public TravelResponse saveTravel(@RequestBody TravelRequest travelRequest) {
        return travelService.saveTravel(travelRequest);
    }

    @GetMapping("")
    public List<TravelResponse> getAllTravel() {
        return travelService.getAllTravel();
    }

//    @GetMapping("/my")
//    public List<TravelResponse> getAllMyTravel(User user) {
//        return travelService.getAllMyTravel(user);
//    }

    @GetMapping("/{tId}")
    public TravelResponse getTravel(@PathVariable("tId") int tId) {
        return travelService.getTravel(tId);
    }

    @PatchMapping("/{tId}")
    public TravelResponse patchTravel(@PathVariable("tId") int tId, @RequestBody TravelRequest travelRequest) {
        return travelService.patchTravel(tId, travelRequest);
    }

    @PostMapping("/{tId}/course")
    public DayCourseResponse saveCourse(@PathVariable("tId") int tId, @RequestBody DayCourseRequest dayCourseRequest) {
        return dayCourseService.saveCourse(dayCourseRequest, tId);
    }



    @GetMapping("/course/{dcId}")
    public DayCourseResponse getCourse(@PathVariable("dcId") int dcId) {
        return dayCourseService.getCourse(dcId);
    }

    @GetMapping("/{tId}/course")
    public List<DayCourseResponse> getAllDayCourseByTravel(@PathVariable("tId") int tId) {
        return dayCourseService.getAllDayCourseByTravel(tId);
    }

    @PostMapping("/course/{dcId}/spot")
    public DayCourseResponse saveSpot(@PathVariable("dcId") int dcId, @RequestBody SpotRequest spotRequest) {
        return spotService.saveSpot(spotRequest, dcId);
    }

    @DeleteMapping("/course/{dcId}/spot/{num}")
    public DayCourseResponse deleteSpot(@PathVariable("dcId") int dcId, @PathVariable("num") int num) {
        return spotService.deleteSpot(dcId, num);
    }

}
