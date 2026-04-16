package com.example.expense.controller;

import com.example.expense.dtos.CreateExpenseRequest;
import com.example.expense.dtos.UpdateExpenseRequest;
import com.example.expense.entity.Expense;
import com.example.expense.service.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<String> createExpense(
            HttpServletRequest request,
            @RequestBody CreateExpenseRequest req) {

        String email = (String) request.getAttribute("userEmail");

        String response = expenseService.createExpense(req, email);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Expense>> getAllExpenses() {

        List<Expense> list = expenseService.getAllExpenses();

        return ResponseEntity.ok(list);
    }

    // 2. Get expenses by name
    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> getById(@PathVariable String expenseId) {

        Expense expense = expenseService.getExpenseById(expenseId);

        return ResponseEntity.ok(expense);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<String> updateExpense(
            @PathVariable String expenseId,
            @RequestBody UpdateExpenseRequest req) {

        String response = expenseService.updateExpense(expenseId, req);

        return ResponseEntity.ok(response);
    }
}
