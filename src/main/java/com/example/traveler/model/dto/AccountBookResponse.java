package com.example.traveler.model.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.traveler.model.entity.AccountBook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountBookResponse {
    private String transactionDetail;
    private double expense;
    private Long accountId;
    private Long tId;
    private double budget;
    private List<TransactionResponse> transactions;
    private Date date;
    private String dateStr;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
        this.date = accountBook.getDate();
        
        setDateStr(sdf.format(this.date));
    }
    
    public void setAccountBook(AccountBook accountBook) {
    	this.expense = accountBook.getTotalExpense();
        this.accountId = accountBook.getAccountId();
        this.tId = (long) accountBook.getTravel().getTId();
        this.budget = accountBook.getTotalBudget();
        this.date = accountBook.getDate();
        
        setDateStr(sdf.format(this.date));
    }
    
    public void setDate(Date date) {
    	this.date = date;
        setDateStr(sdf.format(this.date));
    }


    public AccountBookResponse(AccountBook accountBook, AccountBook accountBook1) {
    }
}