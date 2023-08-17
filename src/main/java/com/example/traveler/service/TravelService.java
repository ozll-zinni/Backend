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

            ChecklistRequest checklistRequest = new ChecklistRequest();
            checklistRequest.setTitle("새로운 체크리스트"); // 체크리스트 제목 설정
            ChecklistResponse checklistResponse = checklistService.saveChecklist(accessToken, saveTravel.getTId(), checklistRequest);
        } catch (Exception e) {
            System.out.println("444444444444");
            throw new BaseException(SAVE_TRAVEL_FAIL);
        }
        System.out.println("3333333333");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TravelResponse travelResponse = new TravelResponse(saveTravel.getTId(), saveTravel.getTitle(), saveTravel.getDestination(), formatter.format(saveTravel.getStart_date()) , formatter.format(saveTravel.getEnd_date()), formatter.format(saveTravel.getCreated_at()), saveTravel.getTimeStatus(), saveTravel.getWriteStatus(), saveTravel.getNoteStatus(), saveTravel.getCourses());
        return travelResponse;
    }

    public TravelResponse getTravel(int tId) throws BaseException {
        Travel getTravel = travelRepository.findBytIdAndState(tId, 1);
        if (getTravel == null) {
            throw new BaseException(TRAVEL_IS_EMPTY);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TravelResponse travelResponse = new TravelResponse(getTravel.getTId(), getTravel.getTitle(), getTravel.getDestination(), formatter.format(getTravel.getStart_date()), formatter.format(getTravel.getEnd_date()), formatter.format(getTravel.getCreated_at()), getTravel.getTimeStatus(), getTravel.getWriteStatus(), getTravel.getNoteStatus(), getTravel.getCourses());
        return travelResponse;
    }

    public List<TravelResponse> getAllTravel() throws BaseException {
        List<Travel> allTravel = travelRepository.findAllByState(1);
        ArrayList<TravelResponse> allTravelResponse = new ArrayList<>();
        for (Travel travel : allTravel) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TravelResponse travelResponse = new TravelResponse(travel.getTId(), travel.getTitle(), travel.getDestination(), formatter.format(travel.getStart_date()), formatter.format(travel.getEnd_date()), formatter.format(travel.getCreated_at()), travel.getTimeStatus(), travel.getWriteStatus(), travel.getNoteStatus(), travel.getCourses());
            allTravelResponse.add(travelResponse);
        }
        return allTravelResponse;
    }

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
                getTravel.setStart_date(travelRequest.getStart_date());
                getTravel.setEnd_date(travelRequest.getEnd_date());
                Travel saveTravel = travelRepository.save(getTravel);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                TravelResponse travelResponse = new TravelResponse(saveTravel.getTId(), saveTravel.getTitle(), saveTravel.getDestination(), formatter.format(saveTravel.getStart_date()), formatter.format(saveTravel.getEnd_date()), formatter.format(saveTravel.getCreated_at()), saveTravel.getTimeStatus(), saveTravel.getWriteStatus(), saveTravel.getNoteStatus(), saveTravel.getCourses());
                return travelResponse;
            } catch (Exception e) {
                throw new BaseException(PATCH_TRAVEL_FAIL);
            }
        }
        else {
            throw new BaseException(TRAVEL_USER_NOT_MATCH);
        }
    }

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
            TravelResponse travelResponse = new TravelResponse(travel.getTId(), travel.getTitle(), travel.getDestination(), formatter.format(travel.getStart_date()), formatter.format(travel.getEnd_date()), formatter.format(travel.getCreated_at()), travel.getTimeStatus(), travel.getWriteStatus(), travel.getNoteStatus(), travel.getCourses());
            allMyTravelResponse.add(travelResponse);
        }
        return allMyTravelResponse;
    }

    public List<TravelResponse> allMyPastTravel(User user) {
        List<Travel> allMyPastTravel = travelRepository.findAllByUserAndStateAndTimeStatus(user, 1, 1);
        ArrayList<TravelResponse> allMyPastTravelResponse = new ArrayList<>();
        for (Travel travel : allMyPastTravel) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TravelResponse travelResponse = new TravelResponse(travel.getTId(), travel.getTitle(), travel.getDestination(), formatter.format(travel.getStart_date()), formatter.format(travel.getEnd_date()), formatter.format(travel.getCreated_at()), travel.getTimeStatus(), travel.getWriteStatus(), travel.getNoteStatus(), travel.getCourses());
            allMyPastTravelResponse.add(travelResponse);
        }
        return allMyPastTravelResponse;
    }

}
