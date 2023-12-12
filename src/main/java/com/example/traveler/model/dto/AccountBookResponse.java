package com.example.traveler.model.dto;

import com.example.traveler.model.entity.AccountBook;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountBookResponse {
    private String transactionDetail;
    private double expense;
    private Long accountId;
    private Long tId;
    private double budget;
    private List<TransactionResponse> transactions;

    public AccountBookResponse() {
        this.accountId = getAccountId();
        this.tId = getTId();
        this.budget = getBudget();
    }

    public AccountBookResponse(AccountBook accountBook) {
        this.expense = accountBook.getTotalExpense();
        this.accountId = accountBook.getAccountId();
        this.tId = (long) accountBook.getTravel().getTId();
        this.budget = accountBook.getTotalBudget();
    }


    public AccountBookResponse(AccountBook accountBook, AccountBook accountBook1) {
    }
}