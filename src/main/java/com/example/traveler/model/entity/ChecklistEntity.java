package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
public class ChecklistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private int CId;

    @Column(nullable = false)
    private String title;

    private int state;

    // TravelEntity와의 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "tId") // tId는 TravelEntity의 PK (여행 ID)
    private Travel travel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checklist")
    private List<ItemEntity> checklistItems;

    // 생성자를 통해 checklistItems 필드를 빈 리스트로 초기화
    public ChecklistEntity() {
        this.checklistItems = new ArrayList<>();
    }

}
