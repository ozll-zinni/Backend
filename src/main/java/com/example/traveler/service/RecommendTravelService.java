package com.example.traveler.service;

import com.example.traveler.model.dto.RecommendTravelRequest;
import com.example.traveler.model.entity.*;
import com.example.traveler.repository.*;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private UserService userService;

    public List<RecommendTravel> getMatchingTravels(String accessToken, RecommendTravelRequest request) {
        int period = calculatePeriod(request.getStartDate(), request.getFinishDate());
        int code1 = request.getCountryId() * 1000 + request.getWhat() * 100 + request.getHard() * 10 ;
        int choose = request.getHard();
        int withwho = request.getWithwho();

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

        Destination t = destinationRepository.findBydId(request.getCountryId());

        User user = userService.getUserByToken(accessToken);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Travel travel = new Travel();

        try {
            Date s = dateFormat.parse(request.getStartDate());
            travel.setStart_date(s);
            Date f = dateFormat.parse(request.getFinishDate());
            travel.setEnd_date(f);
        } catch (ParseException e) {
            System.out.println("");
        }

        travel.setUser(user);
        travel.setWriteStatus(0);
        travel.setDestination(t.getCity());
        travel.setCourses(daycourses);
        travel.setCode(code1);
        travel.setWithWho(withwho);
        travelRepository.save(travel);

        return finalTravels;
    }


    private int calculatePeriod(String startDate, String finishDate) {
        LocalDate s = LocalDate.parse(startDate);
        LocalDate f = LocalDate.parse(finishDate);

        long period = ChronoUnit.DAYS.between(s,f);

        return (int) period + 1;
    }

    public List<Destination> getAllregieon(){
        return destinationRepository.findAll();
    }

    public List<RecommendTravel> getAllrecommendTravel() {
        return recommendTravelRepository.findAll();
    }


}
