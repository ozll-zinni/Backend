package com.example.traveler.repository;

import com.example.traveler.model.entity.ChecklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public
interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {
}