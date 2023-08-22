package com.example.traveler.repository;

import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.model.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
    List<ItemEntity> findAllByChecklist(ChecklistEntity checklist);

    Optional<ItemEntity> findByIdAndChecklist_cId(int id, int cId);

    Optional<ItemEntity> findByIdAndChecklist(int iId, ChecklistEntity checklist); // 수정된 부분

    Optional<ItemEntity> findById(int id);
}
