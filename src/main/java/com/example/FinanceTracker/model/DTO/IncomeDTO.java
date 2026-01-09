package com.example.FinanceTracker.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDTO {
    private String description;
    private String source;
    private Double amount;
    private LocalDate date;
}
