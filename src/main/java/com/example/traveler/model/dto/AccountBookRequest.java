package com.example.traveler.model.dto;

import lombok.Getter;

@Getter
public class AccountBookRequest {
    private String accountName;

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
