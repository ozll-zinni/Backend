package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.ChecklistRequest;
import com.example.traveler.model.dto.ChecklistResponse;
import com.example.traveler.model.dto.ItemRequest;
import com.example.traveler.model.dto.ItemResponse;
import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.model.entity.ItemEntity;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.ChecklistRepository;
import com.example.traveler.repository.ItemRepository;
import com.example.traveler.repository.TravelRepository;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class ChecklistService {
    
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private final ChecklistRepository checklistRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private final ItemRepository itemRepository;

    // 여행정보를 담은 tId를 받아서 해당 여행에 대한 checklist를 조회하고, 없으면 생성하는 메서드
    public ChecklistResponse saveChecklist(String accessToken, Integer tId, ChecklistRequest checklistRequest) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }

        // 주어진 tId에 해당하는 여행 정보를 데이터베이스에서 조회
        Travel travel = travelRepository.findById(tId)
                .orElseThrow(() -> new BaseException(TRAVEL_IS_EMPTY));

        // 해당 여행에 대한 체크리스트를 데이터베이스에서 조회
        ChecklistEntity savedChecklist = checklistRepository.findByTravel(travel);

        // 만약 해당 여행에 대한 체크리스트가 없으면 새로운 체크리스트를 생성하여 저장
        if (savedChecklist == null) {
            ChecklistEntity newChecklist = new ChecklistEntity();
            newChecklist.setTitle("새로운 체크리스트");
            newChecklist.setTravel(travel);

            // 새로운 체크리스트 데이터베이스에 저장
            try {
                savedChecklist = checklistRepository.save(newChecklist);
            } catch (Exception e) {
                throw new BaseException(SAVE_CATEGORY_FAIL);
            }
        }

        // 저장된 체크리스트 정보를 ChecklistResponse 형태로 반환
        ChecklistResponse checklistResponse = new ChecklistResponse(savedChecklist.getTitle(), savedChecklist.getCId(), travel.getTId());

        // 아이템 정보 생성 및 추가
        List<ItemResponse> itemResponses = new ArrayList<>();
        List<ItemRequest> itemRequests = checklistRequest.getItems();
        if (itemRequests != null && !itemRequests.isEmpty()) {
            for (ItemRequest itemRequest : itemRequests) {
                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setName(itemRequest.getName());
                itemEntity.setItemOrder(itemRequest.getItemOrder());
                itemEntity.setIschecked(itemRequest.isChecked());
                itemEntity.setChecklist(savedChecklist); // 아이템과 체크리스트의 관계 설정

                // 아이템을 저장하고 생성된 아이템 ID를 가져옴
                ItemEntity savedItem = itemRepository.save(itemEntity);

                // ItemResponse 객체 생성 및 추가
                ItemResponse itemResponse = new ItemResponse(savedItem.getId(), savedItem.getName(), savedItem.getItemOrder(), savedItem.isIschecked(), savedChecklist.getCId());
                itemResponses.add(itemResponse);
            }
        }
        checklistResponse.setItems(itemResponses);


        return checklistResponse;
    }


    public ChecklistResponse getChecklist(int cId) throws BaseException {
        Optional<ChecklistEntity> getChecklist = checklistRepository.findBycIdAndState(cId, 1);
        if (!getChecklist.isPresent()) { // Optional을 올바르게 체크해야 함
            throw new BaseException(CHECKLIST_IS_EMPTY);
        }
        ChecklistResponse checklistResponse = new ChecklistResponse(getChecklist.get());
        return checklistResponse;
    }


    // 체크리스트명 변경
    public ChecklistResponse patchChecklist(String accessToken, int cId, String newTitle) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        // 기존 체크리스트를 데이터베이스에서 조회
        ChecklistEntity checklist = checklistRepository.findBycId(cId)
                .orElseThrow(() -> new BaseException(CHECKLIST_IS_EMPTY));

        // 체크리스트명 변경
        checklist.setTitle(newTitle);

        // 변경사항을 임시로 저장
        try {
            checklist = checklistRepository.save(checklist);
        } catch (Exception e) {
            throw new BaseException(SAVE_CATEGORY_FAIL);
        }

        // 변경된 체크리스트 정보를 ChecklistResponse 형태로 반환
        ChecklistResponse response = new ChecklistResponse();
        response.setCId(checklist.getCId());
        response.setTitle(checklist.getTitle());

        // tId 정보 추가
        response.setTId(checklist.getTravel().getTId());

        // ItemResponse 리스트 생성 및 추가
        List<ItemResponse> itemResponses = new ArrayList<>();
        for (ItemEntity item : checklist.getChecklistItems()) {
            ItemResponse itemResponse = new ItemResponse();
            itemResponse.setIId(item.getId());
            itemResponse.setName(item.getName());
            itemResponse.setItemOrder(item.getItemOrder());
            itemResponse.setChecked(item.isIschecked());
            itemResponse.setCId(checklist.getCId());
            itemResponses.add(itemResponse);
        }
        response.setItems(itemResponses);

        return response;
    }


    // 체크리스트 삭제
    public int deleteChecklist(String accessToken, int cId) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }

        // 체크리스트를 데이터베이스에서 조회
        ChecklistEntity checklist = checklistRepository.findById(cId)
                .orElseThrow(() -> new BaseException(CHECKLIST_IS_EMPTY));


        try {
            // 체크리스트 삭제 (필요하다면 데이터베이스에서도 삭제)
            checklistRepository.delete(checklist);
        } catch (Exception e) {
            // If the deletion fails, throw an exception indicating the failure
            throw new BaseException(DELETE_CATEGORY_FAIL);
        }

        // 성공적으로 삭제되면 1 반환
        return 1;
    }

    // 특정 여행에 대한 모든 체크리스트를 조회하는 메서드
    public List<ChecklistResponse> getAllChecklistsByTravel(int tId) throws BaseException {

        // 주어진 tId에 해당하는 여행 정보를 데이터베이스에서 조회
        Travel travel = travelRepository.findById(tId)
                .orElseThrow(() -> new BaseException(TRAVEL_IS_EMPTY));

        // 해당 여행에 대한 모든 체크리스트를 데이터베이스에서 조회
        List<ChecklistEntity> checklists = checklistRepository.findAllByTravel(travel);

        // 조회된 체크리스트들을 ChecklistResponse 형태로 변환하여 리스트로 반환
        List<ChecklistResponse> checklistResponses = new ArrayList<>();
        for (ChecklistEntity checklist : checklists) {
            ChecklistResponse response = new ChecklistResponse(checklist);

            // 각 체크리스트에 속한 아이템 정보 추가
            List<ItemResponse> itemResponses = new ArrayList<>();
            for (ItemEntity item : checklist.getChecklistItems()) {
                ItemResponse itemResponse = new ItemResponse();
                itemResponse.setIId(item.getId());
                itemResponse.setName(item.getName());
                itemResponse.setItemOrder(item.getItemOrder()); // 필드 이름 수정
                itemResponse.setChecked(item.isIschecked());
                itemResponse.setCId(checklist.getCId());
                itemResponses.add(itemResponse);
            }
            response.setItems(itemResponses);

            checklistResponses.add(response);
        }

        return checklistResponses;
    }

}