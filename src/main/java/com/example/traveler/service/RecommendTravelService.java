package com.example.traveler.service;

import com.example.traveler.model.dto.RecommendTravelRequest;
import com.example.traveler.model.entity.RecommendTravel;
import com.example.traveler.repository.RecommendTravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RecommendTravelService {

    @Autowired
    private RecommendTravelRepository recommendTravelRepository;

    public List<RecommendTravel> getMatchingTravels(RecommendTravelRequest request) {
        int period = calculatePeriod(request.getStartDate(), request.getFinishDate());
        int code1 = request.getCountryId() * 10000 + request.getWhat() * 1000 + request.getHard() * 100 + request.getWith() * 10 + period;
        int code2 = request.getCountryId() * 10000 + request.getWhat() * 1000 + request.getHard() * 100;

        List<RecommendTravel> matchingTravels = recommendTravelRepository.findByCode1(code1);

        if (matchingTravels.isEmpty()) {
            matchingTravels = recommendTravelRepository.findByCode2(code2);
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
