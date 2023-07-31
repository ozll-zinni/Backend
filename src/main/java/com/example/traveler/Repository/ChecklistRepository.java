package com.example.traveler.Repository;

import com.example.traveler.Model.Entity.CategoryEntity;
import com.example.traveler.Model.Entity.ChecklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public
interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {
}