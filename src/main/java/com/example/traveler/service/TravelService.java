package com.example.traveler.service;

import com.example.traveler.model.dto.TravelRequest;
import com.example.traveler.model.dto.TravelResponse;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TravelService {
    @Autowired
    private TravelRepository travelRepository;

    public TravelResponse saveTravel(TravelRequest travel) {
        Travel newTravel = new Travel(travel.getTitle(), travel.getDestination(), travel.getStart_date(), travel.getEnd_date(), 0, travel.getWrite_status(), 0, 0);
        Travel saveTravel = travelRepository.save(newTravel);
        TravelResponse travelResponse = new TravelResponse(saveTravel.getTId(), saveTravel.getTitle(), saveTravel.getDestination(), saveTravel.getStart_date(), saveTravel.getEnd_date(), saveTravel.getCreated_at(), saveTravel.getTime_status(), saveTravel.getWrite_status(), saveTravel.getNote_status());
        return travelResponse;
    }

    public TravelResponse getTravel(int tId) {
        Travel getTravel = travelRepository.findById(tId);
        TravelResponse travelResponse = new TravelResponse(getTravel.getTId(), getTravel.getTitle(), getTravel.getDestination(), getTravel.getStart_date(), getTravel.getEnd_date(), getTravel.getCreated_at(), getTravel.getTime_status(), getTravel.getWrite_status(), getTravel.getNote_status());
        return travelResponse;
    }

    public List<TravelResponse> getAllTravel() {
        List<Travel> allTravel = travelRepository.findAll();
        ArrayList<TravelResponse> allTravelResponse = new ArrayList<>();
        for (Travel travel : allTravel) {
            TravelResponse travelResponse = new TravelResponse(travel.getTId(), travel.getTitle(), travel.getDestination(), travel.getStart_date(), travel.getEnd_date(), travel.getCreated_at(), travel.getTime_status(), travel.getWrite_status(), travel.getNote_status());
            allTravelResponse.add(travelResponse);
        }
        return allTravelResponse;
    }

    public TravelResponse patchTravel(int tId, TravelRequest travelRequest) {
        Travel getTravel = travelRepository.findById(tId);
        getTravel.setTitle(travelRequest.getTitle());
        getTravel.setStart_date(travelRequest.getStart_date());
        getTravel.setEnd_date(travelRequest.getEnd_date());
        Travel saveTravel = travelRepository.save(getTravel);
        TravelResponse travelResponse = new TravelResponse(saveTravel.getTId(), saveTravel.getTitle(), saveTravel.getDestination(), saveTravel.getStart_date(), saveTravel.getEnd_date(), saveTravel.getCreated_at(), saveTravel.getTime_status(), saveTravel.getWrite_status(), saveTravel.getNote_status());
        return travelResponse;
    }

    public int deleteTravel(int tId) {
        Travel getTravel = travelRepository.findById(tId);
        try {
            travelRepository.delete(getTravel);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
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
