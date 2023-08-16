package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private AccountBook accountBook;
//    private Long dateId;

    @Getter
    private String expenseItem;
    @Getter
    private String description;
    @Getter
    private double amount;
    @Getter
    private String transactionType; // "지출" 또는 "수입"

}

