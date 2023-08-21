package com.example.traveler.repository;

import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.model.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
    public List<ItemEntity> findAllByChecklist(ChecklistEntity checklist);

    Optional<ItemEntity> findByIdAndChecklist_cId(int id, int cId);

    public Optional<Object> findById(int id);
}
