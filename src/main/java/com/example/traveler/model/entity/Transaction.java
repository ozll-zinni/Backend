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

    @JoinColumn(nullable = false)
    private String expenseDetail;

    @JoinColumn(nullable = false)
    private String expenseCategory; // 카테고리

    @JoinColumn(nullable = false)
    private double amount; // 금액

    @ManyToOne
    @JoinColumn(name = "accountId")
    private AccountBook accountBook;

    @Temporal(TemporalType.DATE)
    private Date date;


    public void setAccountBook(AccountBook newAccountBook) {
    }

    public void setAccountbook(AccountBook accountbook) {
        this.accountBook = accountbook;
    }
}

