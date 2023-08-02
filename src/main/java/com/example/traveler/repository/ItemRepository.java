package com.example.traveler.repository;

import com.example.traveler.model.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public
interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}