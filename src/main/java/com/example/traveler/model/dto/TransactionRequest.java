package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {
//    private Long dateId;
    private String expenseItem;
    private String description;
    private double amount;
    private String transactionType; // "expense" 또는 "income"
}