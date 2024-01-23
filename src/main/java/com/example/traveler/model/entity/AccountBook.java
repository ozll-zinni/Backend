package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
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

    @OneToMany(mappedBy = "accountBook")
    private List<Transaction> accountbookTransaction;

    private String accountName;
    private double totalBudget;
    private double totalExpense;
    private double budgetUsagePercentage; // 예산 중 총지출의 비율을 나타내는 필드를 추가
    private double foodExpense;
    private double transportationExpense;
    private double lodgingExpense;
    private double sightseeingExpense;
    private double shoppingExpense;
    private double otherExpense;
    @Temporal(TemporalType.DATE)
    private Date date;

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
        this.budgetUsagePercentage = (totalExpense / totalBudget) * 100;
    }


    public void setUser(User userByToken) {
    }

    public void setDate(Date date) {

        this.date = date;
    }

}

