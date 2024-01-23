package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryResponse {
    private double totalBudget;
    private double totalExpense;
    private double foodExpense;
    private double transportationExpense;
    private double lodgingExpense;
    private double sightseeingExpense;
    private double shoppingExpense;
    private double otherExpense;

    private double budgetUsagePercentage;
    private double foodExpensePercentage;
    private double transportationExpensePercentage;
    private double lodgingExpensePercentage;
    private double sightseeingExpensePercentage;
    private double shoppingExpensePercentage;
    private double otherExpensePercentage;

    // 생성자, getter 및 setter 메서드
}
