package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    @Temporal(TemporalType.DATE)
    private Date date;

    @Getter
    private String expenseItem;
    @Getter
    private String description;
    @Getter
    private double amount;
    @Getter
    private String transactionType; // "지출" 또는 "수입"

    public Transaction(Date date, double v) {
    }
}

