package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.ItemResponse;
import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.repository.ChecklistRepository;
import lombok.AllArgsConstructor;
import com.example.traveler.model.entity.ItemEntity;
import com.example.traveler.model.dto.ItemRequest;
import com.example.traveler.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ChecklistRepository checklistRepository;

//    // 새로운 아이템 정보 저장
//    public ItemResponse saveItem(ItemRequest cId, int itemRequest) throws BaseException {
//        ChecklistEntity checklist = (ChecklistEntity) checklistRepository.findById(cId)
//                .orElseThrow(() -> new BaseException(CHECKLIST_IS_EMPTY));
//
//        ItemEntity newItem = new ItemEntity();
//        newItem.setName(itemRequest.getName());
//        newItem.setOrder(itemRequest.getOrder());
//        newItem.setIschecked(itemRequest.isChecked());
//        newItem.setChecklist(checklist);
//
//        try {
//            newItem = itemRepository.save(newItem);
//        } catch (Exception e) {
//            throw new BaseException(SAVE_ITEM_FAIL);
//        }
//
//        return new ItemResponse(newItem.getIId(), newItem.getName(), newItem.getOrder(), newItem.getIschecked(), cId);
//    }
        // 새로운 아이템 정보 저장
    public ItemResponse saveItem(Long cId, ItemRequest itemRequest) throws BaseException {
        // Fetch the ChecklistEntity using cId
        ChecklistEntity checklist = checklistRepository.findById(cId)
                .orElseThrow(() -> new BaseException(ITEM_NOT_FOUND));

        // Create a new ItemEntity based on the itemRequest
        ItemEntity newItem = new ItemEntity();
        newItem.setName(itemRequest.getName());
        newItem.setIschecked(itemRequest.isChecked());
        newItem.setChecklist(checklist);

        // Save the new item in the database
        newItem = itemRepository.save(newItem);

        // Create and return the ItemResponse
        return new ItemResponse(newItem.getId(), newItem.getName(), newItem.getIschecked() , newItem.getChecklist().getCId());
    }

    // 모든 item 정보 조회
    public List<ItemResponse> getAllItem() {
        List<ItemEntity> items = itemRepository.findAll();
        return mapItemEntityListToItemResponseList(items);
    }

    // 특정 아이템 정보 조회
    public ItemResponse getItem(int iId) throws BaseException {
        ItemEntity item = (ItemEntity) itemRepository.findById(iId)
                .orElseThrow(() -> new BaseException(ITEM_NOT_FOUND));

        return new ItemResponse (item.getId(), item.getName(), item.getIschecked());
    }

    // 특정 checklist에 포함된 모든 item 정보조회
    public List<ItemResponse> getallItemByChecklist(Long cId) throws BaseException {
        ChecklistEntity checklist = checklistRepository.findById(cId)
                .orElseThrow(() -> new BaseException(CHECKLIST_IS_EMPTY));

        List<ItemEntity> items = itemRepository.findAllByChecklist(checklist);
        return mapItemEntityListToItemResponseList(items);
    }

    // ItemEntity 리스트를 ItemResponse 리스트로 변환하는 메서드
    private List<ItemResponse> mapItemEntityListToItemResponseList(List<ItemEntity> items) {
        List<ItemResponse> itemResponses = new ArrayList<>();
        for (ItemEntity item : items) {
            ItemResponse itemResponse = new ItemResponse(item.getId(), item.getName(), item.getIschecked());
            itemResponses.add(itemResponse);
        }
        return itemResponses;
    }


    // 특정 checklist에 포함된 item 수정
    public ItemResponse patchItem(String accessToken, int cId, int iId, ItemRequest itemRequest) throws BaseException {
        ItemEntity item = (ItemEntity) itemRepository.findByIdAndChecklist_CId(iId, cId)
                .orElseThrow(() -> new BaseException(ITEM_NOT_FOUND));

        item.setName(itemRequest.getName());
        item.setIschecked(itemRequest.isChecked());

        try {
            item = itemRepository.save(item);
        } catch (Exception e) {
            throw new BaseException(SAVE_ITEM_FAIL);
        }

        return new ItemResponse(item.getId(), item.getName(), item.getIschecked());
    }


    // 특정 checklist내 포함된 item 삭제
    public int deleteItem(int cId, int iId) throws BaseException {
        ItemEntity item = (ItemEntity) itemRepository.findByIdAndChecklist_CId(iId, cId)
                .orElseThrow(() -> new BaseException(ITEM_NOT_FOUND));

        try {
            itemRepository.delete(item);
        } catch (Exception e) {
            throw new BaseException(DELETE_ITEM_FAIL);
        }
        return cId;
    }

//    // 새로운 아이템 정보 저장
//    public ItemResponse saveItem(int cId, ItemRequest itemRequest) throws BaseException {
//        // Fetch the ChecklistEntity using cId
//        ChecklistEntity checklist = checklistRepository.findById(cId)
//                .orElseThrow(() -> new BaseException(ITEM_NOT_FOUND));
//
//        // Create a new ItemEntity based on the itemRequest
//        ItemEntity newItem = new ItemEntity();
//        newItem.setName(itemRequest.getName());
//        newItem.setIschecked(itemRequest.isChecked());
//        newItem.setChecklist(checklist);
//
//        // Save the new item in the database
//        newItem = itemRepository.save(newItem);
//
//        // Create and return the ItemResponse
//        return new ItemResponse(newItem.getIId(), newItem.getName(), newItem.getIschecked() , newItem.getChecklist().getCId());
//    }

}