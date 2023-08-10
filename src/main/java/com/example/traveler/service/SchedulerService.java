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
        List<Travel> allTravels = travelRepository.findAllByState(1);
        Date date = new Date();
        for (Travel travel : allTravels) {
            if (travel.getStart_date().before(date)) {
                travel.setState(0);
                travelRepository.save(travel);
            }
        }
    }
}
