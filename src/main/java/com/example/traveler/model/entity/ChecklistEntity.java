package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
public class ChecklistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "cid")
    private int cId;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(name = "state")
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
