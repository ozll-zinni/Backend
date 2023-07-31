package com.example.traveler.Repository;

import com.example.traveler.Model.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    // 필요에 따라 카테고리 관련 커스텀 쿼리를 추가할 수 있습니다.
}
