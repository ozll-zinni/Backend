package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "tId") // tId는 TravelEntity의 PK (여행 ID)
    private Travel travel;

    @OneToMany(mappedBy = "accountBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DateEntity> dateEntities = new ArrayList<>();


    @Column(nullable = false)
    private String accountName;

    private double totalBudget;
    private double totalExpense;
    private double foodExpense;
    private double transportationExpense;
    private double sightseeingExpense;
    private double shoppingExpense;
    private double otherExpense;

    @Getter
    @Transient // 데이터베이스에 저장되지 않는 임시 변수로 설정
    private Double foodExpensePercentage;

    @Getter
    @Transient
    private Double transportExpensePercentage;

    @Getter
    @Transient
    private Double sightseeingExpensePercentage;

    @Getter
    @Transient
    private Double shoppingExpensePercentage;

    @Getter
    @Transient
    private Double otherExpensePercentage;

    public void setFoodExpensePercentage(Double foodExpensePercentage) {
        this.foodExpensePercentage = foodExpensePercentage;
    }

    public void setTransportExpensePercentage(Double transportExpensePercentage) {
        this.transportExpensePercentage = transportExpensePercentage;
    }

    public void setSightseeingExpensePercentage(Double sightseeingExpensePercentage) {
        this.sightseeingExpensePercentage = sightseeingExpensePercentage;
    }

    public void setShoppingExpensePercentage(Double shoppingExpensePercentage) {
        this.shoppingExpensePercentage = shoppingExpensePercentage;
    }

    public void setOtherExpensePercentage(Double otherExpensePercentage) {
        this.otherExpensePercentage = otherExpensePercentage;
    }

    // Travel의 시작과 끝 날짜, 몇일차인지 계산하여 DateEntity 추가
    public void addDateEntities() {
        Date startDate = travel.getStart_date();
        Date endDate = travel.getEnd_date();
        int dayCount = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;

        for (int day = 1; day <= dayCount; day++) {
            Date currentDate = new Date(startDate.getTime() + (day - 1) * (1000 * 60 * 60 * 24));
            DateEntity dateEntity = new DateEntity(currentDate, travel, null); // dayCourse는 null로 설정
            dateEntities.add(dateEntity);
        }
    }
}

