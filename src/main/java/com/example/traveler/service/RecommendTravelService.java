package com.example.traveler.service;

import com.example.traveler.model.dto.RecommendTravelRequest;
import com.example.traveler.model.entity.RecommendTravel;
import com.example.traveler.repository.RecommendTravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class RecommendTravelService {

    @Autowired
    private RecommendTravelRepository recommendTravelRepository;


    public List<RecommendTravel> getMatchingTravels(RecommendTravelRequest request) {
        int period = calculatePeriod(request.getStartDate(), request.getFinishDate());
        int code1 = request.getCountryId() * 1000 + request.getWhat() * 100 + request.getHard() * 10 ;


        List<RecommendTravel> matchingTravels = new ArrayList<>();

        for (int i = 1; i <= period; i++) {
            int code = code1 + i;

            List<RecommendTravel> travelcode1 = recommendTravelRepository.findByCode1(code);

            matchingTravels.addAll(travelcode1);

        }

        return matchingTravels;
    }

    private int calculatePeriod(String startDate, String finishDate) {
        LocalDate s = LocalDate.parse(startDate);
        LocalDate f = LocalDate.parse(finishDate);

        long period = ChronoUnit.DAYS.between(s,f);

        return (int) period + 1;
    }

}
