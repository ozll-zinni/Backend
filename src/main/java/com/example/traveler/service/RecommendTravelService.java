package com.example.traveler.service;

import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.RecommendTravelRequest;
import com.example.traveler.model.entity.DayCourse;
import com.example.traveler.model.entity.RecommendTravel;
import com.example.traveler.model.entity.Spot;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.DayCourseRepository;
import com.example.traveler.repository.RecommendTravelRepository;
import com.example.traveler.repository.SpotRepository;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RecommendTravelService {

    @Autowired
    private RecommendTravelRepository recommendTravelRepository;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private DayCourseRepository dayCourseRepository;

    @Autowired
    private TravelRepository travelRepository;


    public List<RecommendTravel> getMatchingTravels(RecommendTravelRequest request) {
        int period = calculatePeriod(request.getStartDate(), request.getFinishDate());
        int code1 = request.getCountryId() * 1000 + request.getWhat() * 100 + request.getHard() * 10 ;
        int choose = request.getHard();
        int with = request.getWith();

        List<RecommendTravel> finalTravels = new ArrayList<>();

        int num = 0;

        switch (choose) {
            case 1:
                num += 4;
                break;
            case 2:
                num += 3;
                break;
            case 3:
                num += 2;
                break;
        }

        for (int i = 1; i <= period; i++) {
            int code = code1 + i;

            List<RecommendTravel> travelcode1 = recommendTravelRepository.findByCode1(code);

            Collections.shuffle(travelcode1);

            List<RecommendTravel> randomResult = travelcode1.subList(0, num);

            finalTravels.addAll(randomResult);

        }

        List<Spot> spots = new ArrayList<>();
        List<DayCourse> daycourses = new ArrayList<>();

        int snum = 1;

        for (RecommendTravel travel : finalTravels) {

            Spot spot = new Spot(travel.getDestination(), travel.getLatitude(), travel.getLongitude());
            spotRepository.save(spot);
            spots.add(spot);

            if (snum%num == 0) {

                int numOfDay = travel.getCode1() % 10;

                DayCourse dayCourse = new DayCourse();

                switch (num) {
                    case 1:
                        dayCourse.setSpot1(spots.get(0));
                        break;
                    case 2:
                        dayCourse.setSpot1(spots.get(0));
                        dayCourse.setSpot2(spots.get(1));
                        break;
                    case 3:
                        dayCourse.setSpot1(spots.get(0));
                        dayCourse.setSpot2(spots.get(1));
                        dayCourse.setSpot3(spots.get(2));
                        break;
                    case 4:
                        dayCourse.setSpot1(spots.get(0));
                        dayCourse.setSpot2(spots.get(1));
                        dayCourse.setSpot3(spots.get(2));
                        dayCourse.setSpot4(spots.get(3));
                        break;
                }

                dayCourse.setNumOfDay(numOfDay);
                dayCourseRepository.save(dayCourse);
                daycourses.add(dayCourse);
                spots.clear();
                snum = 0;

            }

            snum += 1;

        }

        Travel travel = new Travel();
        travel.setCourses(daycourses);
        travel.setCode(code1);
        travel.setWithWho(with);
        travelRepository.save(travel);

        return finalTravels;
    }


    private int calculatePeriod(String startDate, String finishDate) {
        LocalDate s = LocalDate.parse(startDate);
        LocalDate f = LocalDate.parse(finishDate);

        long period = ChronoUnit.DAYS.between(s,f);

        return (int) period + 1;
    }


}
