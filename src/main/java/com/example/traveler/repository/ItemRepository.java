package com.example.traveler.repository;

import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.model.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    public List<ItemEntity> findAllByChecklist(ChecklistEntity checklist);

    public Optional<Object> findByIdAndChecklist_CId(int iId, int cId);

    public Optional<Object> findById(int id);
}
