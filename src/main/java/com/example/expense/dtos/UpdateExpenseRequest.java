package com.example.expense.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateExpenseRequest {

    public Double amount;
    public LocalDate date;
    public String description;
}
