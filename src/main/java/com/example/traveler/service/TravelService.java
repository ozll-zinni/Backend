package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.ChecklistRequest;
import com.example.traveler.model.dto.ChecklistResponse;
import com.example.traveler.model.dto.TravelRequest;
import com.example.traveler.model.dto.TravelResponse;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static com.example.traveler.config.BaseResponseStatus.*;

@Service
public class TravelService {
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChecklistService checklistService;

    @Transactional
    public TravelResponse saveTravel(String accessToken, TravelRequest travel) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            System.out.println("11111111");
            throw new BaseException(INVALID_JWT);

        }
        Travel saveTravel = null;
        try {
            System.out.println("2222222222");
            SimpleDateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            Travel newTravel = new Travel(travel.getTitle(), travel.getDestination(), travel.getStart_date(), travel.getEnd_date(), 0, travel.getWriteStatus(), 0, 1, user);
            saveTravel = travelRepository.save(newTravel);

        } catch (Exception e) {
            System.out.println("444444444444");
            throw new BaseException(SAVE_TRAVEL_FAIL);
        }
        System.out.println("3333333333");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedStartDate = saveTravel.getStartDate() == null ? null : formatter.format(saveTravel.getStartDate());
        String formattedEndDate = saveTravel.getEndDate() == null ? null : formatter.format(saveTravel.getEndDate());
        String formattedCreatedAt = saveTravel.getCreatedAt() == null ? null : formatter.format(saveTravel.getCreatedAt());

        TravelResponse travelResponse = new TravelResponse(saveTravel.getTId(), saveTravel.getTitle(), saveTravel.getDestination(), formattedStartDate, formattedEndDate, formattedCreatedAt, saveTravel.getTimeStatus(), saveTravel.getWriteStatus(), saveTravel.getNoteStatus(), saveTravel.getCourses(), saveTravel.getUser().getId(), saveTravel.getCode());
        return travelResponse;
    }

    public TravelResponse getTravel(int tId) throws BaseException {
        Travel getTravel = travelRepository.findBytIdAndState(tId, 1);
        if (getTravel == null) {
            throw new BaseException(TRAVEL_IS_EMPTY);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TravelResponse travelResponse = new TravelResponse(getTravel.getTId(), getTravel.getTitle(), getTravel.getDestination(), formatter.format(getTravel.getStartDate()), formatter.format(getTravel.getEndDate()), formatter.format(getTravel.getCreatedAt()), getTravel.getTimeStatus(), getTravel.getWriteStatus(), getTravel.getNoteStatus(), getTravel.getCourses(), getTravel.getUser().getId(), getTravel.getCode());
        return travelResponse;
    }

    public List<TravelResponse> getAllTravel() throws BaseException {
        List<Travel> allTravel = travelRepository.findAllByState(1);
        ArrayList<TravelResponse> allTravelResponse = new ArrayList<>();
        for (Travel travel : allTravel) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TravelResponse travelResponse = new TravelResponse(travel.getTId(), travel.getTitle(), travel.getDestination(), formatter.format(travel.getStartDate()), formatter.format(travel.getEndDate()), formatter.format(travel.getCreatedAt()), travel.getTimeStatus(), travel.getWriteStatus(), travel.getNoteStatus(), travel.getCourses(), travel.getUser().getId(), travel.getCode());
            allTravelResponse.add(travelResponse);
        }
        return allTravelResponse;
    }


    @Transactional
    public TravelResponse patchTravel(String accessToken, int tId, TravelRequest travelRequest) throws BaseException{
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        Travel getTravel = travelRepository.findBytIdAndState(tId, 1);
        if (getTravel == null) {
            throw new BaseException(TRAVEL_IS_EMPTY);
        }
        if (user == getTravel.getUser()) {
            try {
                getTravel.setTitle(travelRequest.getTitle());
                getTravel.setStartDate(travelRequest.getStart_date());
                getTravel.setEndDate(travelRequest.getEnd_date());
                Travel saveTravel = travelRepository.save(getTravel);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                TravelResponse travelResponse = new TravelResponse(saveTravel.getTId(), saveTravel.getTitle(), saveTravel.getDestination(), formatter.format(saveTravel.getStartDate()), formatter.format(saveTravel.getEndDate()), formatter.format(saveTravel.getCreatedAt()), saveTravel.getTimeStatus(), saveTravel.getWriteStatus(), saveTravel.getNoteStatus(), saveTravel.getCourses(), saveTravel.getUser().getId(), saveTravel.getCode());
                return travelResponse;
            } catch (Exception e) {
                throw new BaseException(PATCH_TRAVEL_FAIL);
            }
        }
        else {
            throw new BaseException(TRAVEL_USER_NOT_MATCH);
        }
    }


    @Transactional
    public int deleteTravel(String accessToken, int tId) throws BaseException{
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        Travel getTravel = travelRepository.findBytIdAndState(tId, 1);
        if (getTravel == null) {
            throw new BaseException(TRAVEL_IS_EMPTY);
        }
        try {
            getTravel.setState(0);
            travelRepository.save(getTravel);
        } catch (Exception e) {
            throw new BaseException(DELETE_TRAVEL_FAIL);
        }
        return 1;
    }

    public List<TravelResponse> getAllMyTravel(User user) {
        List<Travel> allMyTravel = travelRepository.findAllByUserAndState(user, 1);
        ArrayList<TravelResponse> allMyTravelResponse = new ArrayList<>();
        for (Travel travel : allMyTravel) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TravelResponse travelResponse = new TravelResponse(travel.getTId(), travel.getTitle(), travel.getDestination(), formatter.format(travel.getStartDate()), formatter.format(travel.getEndDate()), formatter.format(travel.getCreatedAt()), travel.getTimeStatus(), travel.getWriteStatus(), travel.getNoteStatus(), travel.getCourses(), travel.getUser().getId(), travel.getCode());
            allMyTravelResponse.add(travelResponse);
        }
        return allMyTravelResponse;
    }

    public List<TravelResponse> allMyPastTravel(User user) {
        List<Travel> allMyPastTravel = travelRepository.findAllByUserAndStateAndTimeStatus(user, 1, 1);
        ArrayList<TravelResponse> allMyPastTravelResponse = new ArrayList<>();
        for (Travel travel : allMyPastTravel) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TravelResponse travelResponse = new TravelResponse(travel.getTId(), travel.getTitle(), travel.getDestination(), formatter.format(travel.getStartDate()), formatter.format(travel.getEndDate()), formatter.format(travel.getCreatedAt()), travel.getTimeStatus(), travel.getWriteStatus(), travel.getNoteStatus(), travel.getCourses(), travel.getUser().getId(), travel.getCode());
            allMyPastTravelResponse.add(travelResponse);
        }
        return allMyPastTravelResponse;
    }

}
