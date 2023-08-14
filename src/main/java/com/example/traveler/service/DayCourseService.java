package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.DayCourseRequest;
import com.example.traveler.model.dto.DayCourseResponse;
import com.example.traveler.model.entity.DayCourse;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.DayCourseRepository;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.traveler.config.BaseResponseStatus.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.INVALID_JWT;
import static com.example.traveler.config.BaseResponseStatus.TRAVEL_USER_NOT_MATCH;

@Service
public class DayCourseService {
    @Autowired
    private DayCourseRepository dayCourseRepository;
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private UserService userService;
    public DayCourseResponse saveCourse(String accessToken, DayCourseRequest course, int tId) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        Travel travel = travelRepository.findBytIdAndState(tId, 1);
        if (travel.getUser() == user) {
            try {
                if (dayCourseRepository.findByTravelAndNumOfDay(travel, course.getNumOfDay()) == null) {
                    DayCourse dayCourse = new DayCourse(travel, course.getNumOfDay());
                    DayCourse newDayCourse = dayCourseRepository.save(dayCourse);
                    DayCourseResponse dayCourseResponse = new DayCourseResponse(newDayCourse.getDcId(), dayCourse.getTravel().getTId(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), newDayCourse.getNumOfDay());
                    return dayCourseResponse;
                }
                else {
                    throw new BaseException(DAYCOURSE_EXISTS);
                }
            } catch (Exception e) {
                throw new BaseException(SAVE_DAYCOURSE_FAIL);
            }
        }
        else {
            throw new BaseException(TRAVEL_USER_NOT_MATCH);
        }
    }

    public DayCourseResponse getCourse(int dcId) throws BaseException{
        DayCourse dayCourse = dayCourseRepository.findBydcId(dcId);
        if (dayCourse == null) {
            throw new BaseException(DAYCOURSE_IS_EMPTY);
        }
        DayCourseResponse dayCourseResponse = new DayCourseResponse(dayCourse.getDcId(), dayCourse.getTravel().getTId(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), dayCourse.getNumOfDay());
        return dayCourseResponse;
    }

    public List<DayCourseResponse> getAllDayCourseByTravel(int tId) throws BaseException{
        Travel travel = travelRepository.findBytIdAndState(tId, 1);
        List<DayCourse> allDayCourse = dayCourseRepository.findAllByTravelOrderByNumOfDay(travel);
        ArrayList<DayCourseResponse> allDayCourseResponse = new ArrayList<>();
        for (DayCourse dayCourse : allDayCourse) {
            DayCourseResponse dayCourseResponse = new DayCourseResponse(dayCourse.getDcId(), dayCourse.getTravel().getTId(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), dayCourse.getNumOfDay());
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
