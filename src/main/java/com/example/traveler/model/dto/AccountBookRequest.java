package com.example.traveler.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AccountBookRequest {
    private Long accountId;
    private int tId; // 여행 ID
    private Double totalBudget; // 전체 예산
    private List<DailyAccountBookRequest> dailyAccountBooks; // 일일 가계부 리스트
}
