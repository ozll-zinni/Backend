package com.example.traveler.service;

import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SchedulerService {
    @Autowired
    private TravelRepository travelRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleTaskingForPastTravel() {
        System.out.println("스케쥴링!");
        List<Travel> allTravels = travelRepository.findAllByStateAndTimeStatus(1, 0);
        Date date = new Date();
        System.out.println("오늘 날짜 : " + date);
        for (Travel travel : allTravels) {
            if (travel.getStartDate().before(date)) {
                System.out.println("오늘보다 전이면");
                travel.setTimeStatus(1);
                System.out.println(travel.getTId() + ": 지난 여행 처리!");
                travelRepository.save(travel);
            }
        }
    }
}
