package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.DayCourseRequest;
import com.example.traveler.model.dto.DayCourseResponse;
import com.example.traveler.model.dto.SpotRequest;
import com.example.traveler.model.entity.DayCourse;
import com.example.traveler.    model.entity.Spot;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.DayCourseRepository;
import com.example.traveler.repository.SpotRepository;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.traveler.config.BaseResponseStatus.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.INVALID_JWT;

@Service
public class SpotService {
    @Autowired
    private DayCourseRepository dayCourseRepository;
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private SpotRepository spotRepository;
    @Autowired
    private UserService userService;
    public DayCourseResponse saveSpot(String accessToken, SpotRequest spotRequest, int dcId) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        DayCourse dayCourse = dayCourseRepository.findBydcId(dcId);
        if (dayCourse == null) {
            throw new BaseException(DAYCOURSE_IS_EMPTY);
        }
        Spot saveSpot = null;
        try {
            Spot spot = new Spot(spotRequest.getTitle(), spotRequest.getLatitude(), spotRequest.getLongitude());
            saveSpot = spotRepository.save(spot);
        } catch (Exception e) {
            throw new BaseException(SAVE_SPOT_FAIL);
        }

        if (dayCourse.getSpot1() == null) {
            dayCourse.setSpot1(saveSpot);
            System.out.println("111111111111111111111111111");
        } else if (dayCourse.getSpot2() == null) {
            //거리계산
            dayCourse.setSpot2(saveSpot);
            dayCourse.setFirst(distance(dayCourse.getSpot1().getLatitude(), dayCourse.getSpot1().getLongitude(), dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude()));
            System.out.println("2222222222222222222222222222222");
        } else if (dayCourse.getSpot3() == null) {
            //거리계산
            dayCourse.setSpot3(saveSpot);
            dayCourse.setSecond(distance(dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude(), dayCourse.getSpot3().getLatitude(), dayCourse.getSpot3().getLongitude()));
            System.out.println("333333333333333333333333333333333");
        } else if (dayCourse.getSpot4() == null) {
            //거리계산
            dayCourse.setSpot4(saveSpot);
            dayCourse.setThird(distance(dayCourse.getSpot3().getLatitude(), dayCourse.getSpot3().getLongitude(), dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude()));
            System.out.println("44444444444444444444444444444444444");
        } else {
            throw new BaseException(SPOT_IS_FULL);
        }
        DayCourse newDayCourse = null;
        try {
            newDayCourse = dayCourseRepository.save(dayCourse);
        } catch (Exception e) {
            throw new BaseException(SAVE_DAYCOURSE_FAIL);
        }
        DayCourseResponse dayCourseResponse = new DayCourseResponse(newDayCourse.getDcId(), newDayCourse.getTravel().getTId(), dayCourse.getSpot1(), dayCourse.getSpot2(), dayCourse.getSpot3(), dayCourse.getSpot4(), dayCourse.getFirst(), dayCourse.getSecond(), dayCourse.getThird(), newDayCourse.getNumOfDay());
        return dayCourseResponse;
    }

    public DayCourseResponse deleteSpot(String accessToken, int dcId, int num) throws BaseException{
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        DayCourse dayCourse = dayCourseRepository.findBydcId(dcId);
        if (dayCourse == null) {
            throw new BaseException(DAYCOURSE_IS_EMPTY);
        }
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
        } else {
            throw new BaseException(DELETE_SPOT_FAIL);
        }
        if (dayCourse.getSpot1() != null && dayCourse.getSpot2() != null) {
            dayCourse.setFirst(distance(dayCourse.getSpot1().getLatitude(), dayCourse.getSpot1().getLongitude(), dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude()));
        }
        else {
            dayCourse.setFirst(null);
        }
        if (dayCourse.getSpot2() != null && dayCourse.getSpot3() != null) {
            dayCourse.setSecond(distance(dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude(), dayCourse.getSpot3().getLatitude(), dayCourse.getSpot3().getLongitude()));
        }
        else {
            dayCourse.setSecond(null);
        }
        if (dayCourse.getSpot3() != null && dayCourse.getSpot4() != null) {
            dayCourse.setThird(distance(dayCourse.getSpot3().getLatitude(), dayCourse.getSpot3().getLongitude(), dayCourse.getSpot2().getLatitude(), dayCourse.getSpot2().getLongitude()));
        }
        else {
            dayCourse.setThird(null);
        }

        DayCourse newDayCourse = null;
        try {
            newDayCourse = dayCourseRepository.save(dayCourse);
        } catch (Exception e) {
            throw new BaseException(SAVE_DAYCOURSE_FAIL);
        }
        DayCourseResponse dayCourseResponse = new DayCourseResponse(newDayCourse.getDcId(), newDayCourse.getTravel().getTId(), newDayCourse.getSpot1(), newDayCourse.getSpot2(), newDayCourse.getSpot3(), newDayCourse.getSpot4(), newDayCourse.getFirst(), newDayCourse.getSecond(), newDayCourse.getThird(), newDayCourse.getNumOfDay());
        return dayCourseResponse;
    }

    //경도, 위도를 이용한 거리 계산
    public static double distance(double lat1, double lon1, double lat2, double lon2){

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return dist; //단위 meter
    }

    //10진수를 radian(라디안)으로 변환
    public static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    public static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }

}
