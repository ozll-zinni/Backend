package com.example.traveler.repository;

import com.example.traveler.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
    // 필요에 따라 카테고리 관련 커스텀 쿼리를 추가할 수 있습니다.
}
