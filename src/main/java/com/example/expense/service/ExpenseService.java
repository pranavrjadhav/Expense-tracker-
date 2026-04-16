package com.example.expense.service;

import com.example.expense.dtos.CreateExpenseRequest;
import com.example.expense.dtos.UpdateExpenseRequest;
import com.example.expense.entity.Expense;
import com.example.expense.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public static String generateExpenseId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public String createExpense(CreateExpenseRequest req, String userEmail) {

        Expense expense = Expense.builder()
                .expenseId(generateExpenseId())
                .name(req.name)
                .amount(req.amount)
                .date(req.date)
                .description(req.description)
                .userEmail(userEmail)
                .build();

        expenseRepository.save(expense);

        return "Expense created successfully with ID: " + expense.getExpenseId();

    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(String expenseId) {

        return expenseRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public String updateExpense(String expenseId, UpdateExpenseRequest req) {

        Expense expense = expenseRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (req.amount != null) {
            expense.setAmount(req.amount);
        }

        if (req.date != null) {
            expense.setDate(req.date);
        }

        if (req.description != null) {
            expense.setDescription(req.description);
        }

        expenseRepository.save(expense);

        return "Expense updated successfully";
    }
}
