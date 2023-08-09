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

@Service
public class RecommendTravelService {

    @Autowired
    private RecommendTravelRepository recommendTravelRepository;

    public List<RecommendTravel> getMatchingTravels(RecommendTravelRequest request) {
        int period = calculatePeriod(request.getStartDate(), request.getFinishDate());
        int code1 = request.getCountryId() * 10000 + request.getWhat() * 1000 + request.getHard() * 100 + request.getWith() * 10;
        int code2 = request.getCountryId() * 100 + request.getWhat() * 10 + request.getHard() * 1;

        List<RecommendTravel> matchingTravels = new ArrayList<>();

        for (int i = 1; i <= period; i++) {
            int code = code1 + i;

            List<RecommendTravel> travelcode1 = recommendTravelRepository.findByCode1(code);

            if(!travelcode1.isEmpty()){
                matchingTravels.addAll(travelcode1);
            }

            else if (travelcode1.isEmpty()) {
                List<RecommendTravel> travelcode2 = recommendTravelRepository.findByCode2(code2);
                matchingTravels.addAll(travelcode2);
            }
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
