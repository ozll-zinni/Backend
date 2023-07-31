package com.example.traveler.repository;

import com.example.traveler.model.entity.CategoryEntity;
import com.example.traveler.model.entity.ChecklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {
    Optional<ChecklistEntity> findByCategoryAndId(CategoryEntity category, Long itemId);
    List<ChecklistEntity> findByCategoryOrderByOrderAsc(CategoryEntity category);
}
