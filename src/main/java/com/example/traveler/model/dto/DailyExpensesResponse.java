package com.example.traveler.model.dto;

import java.util.Map;

public class DailyExpensesResponse {
    private Map<String, Double> dailyExpenses;

    public DailyExpensesResponse(Map<String, Double> dailyExpenses) {
        this.dailyExpenses = dailyExpenses;
    }

    public Map<String, Double> getDailyExpenses() {
        return dailyExpenses;
    }

    public void setDailyExpenses(Map<String, Double> dailyExpenses) {
        this.dailyExpenses = dailyExpenses;
    }
}
