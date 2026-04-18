package com.example.expense.service;

import com.example.expense.dtos.CreateExpenseRequest;
import com.example.expense.dtos.ExpensePageResponse;
import com.example.expense.dtos.ExpenseResponse;
import com.example.expense.dtos.UpdateExpenseRequest;
import com.example.expense.entity.Expense;
import com.example.expense.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public ExpensePageResponse getAllExpenses(Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "date")
        );

        Page<Expense> page = expenseRepository.findAll(sortedPageable);

        List<ExpenseResponse> list = page.getContent().stream().map(exp -> {
            ExpenseResponse res = new ExpenseResponse();
            res.id = exp.getExpenseId();
            res.name = exp.getName();
            res.amount = exp.getAmount();
            res.date = exp.getDate();
            return res;
        }).toList();

        ExpensePageResponse response = new ExpensePageResponse();
        response.data = list;
        response.page = page.getNumber();
        response.size = page.getSize();
        response.totalElements = page.getTotalElements();
        response.totalPages = page.getTotalPages();

        return response;
    }

    public Expense getExpenseById(String expenseId) {

        return expenseRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public String updateExpense(String expenseId, UpdateExpenseRequest req) {

        Expense expense = expenseRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if(req.name != null){
            expense.setName(req.name);
        }
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
