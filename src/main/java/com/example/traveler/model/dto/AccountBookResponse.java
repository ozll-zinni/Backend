package com.example.traveler.model.dto;

import com.example.traveler.model.entity.AccountBook;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountBookResponse {
    private String accountName;
    private Long accountId;
    private Long tId; // 여행 I
    private double budget;
    private List<TransactionResponse> transactions;

    public AccountBookResponse() {
        this.accountName = getAccountName();
        this.accountId = getAccountId();
        this.tId = getTId();
        this.budget = getBudget();
    }

    public AccountBookResponse(String accountName, Long accountId) {
    }

    public AccountBookResponse(AccountBook accountBook, AccountBook accountBook1) {
    }
}