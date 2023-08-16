package com.example.traveler.model.dto;

import com.example.traveler.model.entity.AccountBook;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountBookResponse {
    private String accountName;
    private Long accountId;

    public AccountBookResponse() {
        this.accountName = getAccountName();
        this.accountId = getAccountId();
    }

    public AccountBookResponse(String accountName, Long accountId) {
    }

    public AccountBookResponse(AccountBook accountBook, AccountBook accountBook1) {
    }
}