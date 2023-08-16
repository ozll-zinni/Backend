package com.example.traveler.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AccountBookRequest {
    private String accountName;
    private Long accountId;
    private Long tId; // 여행 ID
    private double budget;
    private List<TransactionRequest> transactions;

    public AccountBookRequest(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
