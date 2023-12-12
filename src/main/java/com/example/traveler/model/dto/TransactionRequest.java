package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {
    private String expenseCategory; // 비용 카테고리 (식비, 교통비, 숙박비, 관광비, 쇼핑비, 기타)
    private String expenseDetail; // 비용 세부사항
    private Double amount; // 비용 금액
}