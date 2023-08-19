package com.example.traveler.model.dto;

import java.util.Map;

public class MonthlyExpensesResponse {
    private Map<String, Double> monthlyExpenses;

    public MonthlyExpensesResponse(Map<String, Double> monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public Map<String, Double> getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(Map<String, Double> monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }
}

