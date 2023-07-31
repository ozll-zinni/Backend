package com.example.traveler.service;

import com.example.traveler.model.dto.DayCourseRequest;
import com.example.traveler.model.dto.DayCourseResponse;
import com.example.traveler.model.dto.TravelRequest;
import com.example.traveler.model.dto.TravelResponse;
import com.example.traveler.model.entity.DayCourse;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.DayCourseRepository;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DayCourseService {
    @Autowired
    private DayCourseRepository dayCourseRepository;
    @Autowired
    private TravelRepository travelRepository;
    public DayCourseResponse saveCourse(DayCourseRequest course, int tId) {
        Travel travel = travelRepository.findById(tId);
        DayCourse dayCourse = new DayCourse(travel, course.getNumOfDay());
        DayCourse newDayCourse = dayCourseRepository.save(dayCourse);
        DayCourseResponse dayCourseResponse = new DayCourseResponse(newDayCourse.getDcId(), newDayCourse.getTravel(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), newDayCourse.getNumOfDay());
        return dayCourseResponse;
    }

    public DayCourseResponse getCourse(int dcId) {
        DayCourse dayCourse = dayCourseRepository.findById(dcId);
        DayCourseResponse dayCourseResponse = new DayCourseResponse(dayCourse.getDcId(), dayCourse.getTravel(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), dayCourse.getNumOfDay());
        return dayCourseResponse;
    }

    public List<DayCourseResponse> getAllDayCourseByTravel(int tId) {
        Travel travel = travelRepository.findById(tId);
        List<DayCourse> allDayCourse = dayCourseRepository.findAllByTravel(travel);
        ArrayList<DayCourseResponse> allDayCourseResponse = new ArrayList<>();
        for (DayCourse dayCourse : allDayCourse) {
            DayCourseResponse dayCourseResponse = new DayCourseResponse(dayCourse.getDcId(), dayCourse.getTravel(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), dayCourse.getNumOfDay());
            allDayCourseResponse.add(dayCourseResponse);
        }
        return allDayCourseResponse;
    }

//    public List<TravelResponse> getAllMyTravel(User user) {
//        List<Travel> allMyTravel = travelRepository.findAllByUser(user);
//        ArrayList<TravelResponse> allMyTravelResponse = new ArrayList<>();
//        for (Travel travel : allMyTravel) {
//            TravelResponse travelResponse = new TravelResponse(travel.getTId(), travel.getTitle(), travel.getDestination(), travel.getStart_date(), travel.getEnd_date(), travel.getCreated_at(), travel.getTime_status(), travel.getWrite_status(), travel.getNote_status());
//            allMyTravelResponse.add(travelResponse);
//        }
//        return allMyTravelResponse;
//    }

}
