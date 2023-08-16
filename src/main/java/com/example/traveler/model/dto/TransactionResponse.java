package com.example.traveler.model.dto;

import com.example.traveler.model.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
    private Long transactionId;
//    private Long dateId;
    private String expenseCategory; // 지출 항목
    private String description; //내용
    private double amount;
    private String transactionType;

    public TransactionResponse(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
//        this.dateId = transaction.getDateId();
        this.expenseCategory = transaction.getExpenseItem();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.transactionType = transaction.getTransactionType();
    }
}
