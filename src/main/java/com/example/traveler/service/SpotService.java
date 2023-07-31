package com.example.traveler.service;

import com.example.traveler.model.dto.DayCourseRequest;
import com.example.traveler.model.dto.DayCourseResponse;
import com.example.traveler.model.dto.SpotRequest;
import com.example.traveler.model.entity.DayCourse;
import com.example.traveler.model.entity.Spot;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.DayCourseRepository;
import com.example.traveler.repository.SpotRepository;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpotService {
    @Autowired
    private DayCourseRepository dayCourseRepository;
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private SpotRepository spotRepository;
    public DayCourseResponse saveSpot(SpotRequest spotRequest, int dcId) {
        DayCourse dayCourse = dayCourseRepository.findBydcId(dcId);
        Spot spot = new Spot(spotRequest.getTitle(), spotRequest.getLatitude(), spotRequest.getLongitude());
        Spot saveSpot = spotRepository.save(spot);
        if (dayCourse.getSpot1() == null) {
            dayCourse.setSpot1(saveSpot);
        } else if (dayCourse.getSpot2() == null) {
            //거리계산
            dayCourse.setSpot2(saveSpot);
        } else if (dayCourse.getSpot3() == null) {
            //거리계산
            dayCourse.setSpot3(saveSpot);
        } else if (dayCourse.getSpot4() == null) {
            //거리계산
            dayCourse.setSpot4(saveSpot);
        }
        DayCourse newDayCourse = dayCourseRepository.save(dayCourse);
        DayCourseResponse dayCourseResponse = new DayCourseResponse(newDayCourse.getDcId(), newDayCourse.getTravel(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), newDayCourse.getNumOfDay());
        return dayCourseResponse;
    }

    public DayCourseResponse deleteSpot(int dcId, int num) {
        DayCourse dayCourse = dayCourseRepository.findBydcId(dcId);
        if (num == 1) {
            dayCourse.setSpot1(dayCourse.getSpot2());
            dayCourse.setSpot2(dayCourse.getSpot3());
            dayCourse.setSpot3(dayCourse.getSpot4());
            dayCourse.setSpot4(null);
        } else if (num == 2) {
            dayCourse.setSpot2(dayCourse.getSpot3());
            dayCourse.setSpot3(dayCourse.getSpot4());
            dayCourse.setSpot4(null);
        } else if (num == 3) {
            dayCourse.setSpot3(dayCourse.getSpot4());
            dayCourse.setSpot4(null);
        } else if (num == 4) {
            dayCourse.setSpot4(null);
        }
        DayCourse newDayCourse = dayCourseRepository.save(dayCourse);
        DayCourseResponse dayCourseResponse = new DayCourseResponse(newDayCourse.getDcId(), newDayCourse.getTravel(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), newDayCourse.getNumOfDay());
        return dayCourseResponse;
    }

}
