package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class DailyAccountBookRequest {
    private Integer accountId; // 가계부 ID
    private List<TransactionRequest> transactions; // 거래 내역 리스트

    public List<TransactionRequest> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<TransactionRequest> transactions) {
        this.transactions = transactions;
    }

    public Integer getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Date getDate() {
        return this.getDate();
    }
}