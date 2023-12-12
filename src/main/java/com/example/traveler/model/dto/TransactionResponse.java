package com.example.traveler.model.dto;

import com.example.traveler.model.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
    private Long transactionId;
    private String expenseCategory; // 지출 항목
    private String expenseDetail; //내용
    private double amount;

    public TransactionResponse(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.expenseCategory = transaction.getExpenseCategory();
        this.expenseDetail = transaction.getExpenseDetail() ;
        this.amount = transaction.getAmount();
    }

    public  TransactionResponse(Long transactionId, String expenseCategory, String expenseDetail, double amount) {
        this.transactionId = transactionId;
        this.expenseCategory = expenseCategory;
        this.expenseDetail = expenseDetail;
        this.amount = amount;
    }

    public TransactionResponse() {

    }
}
