package com.example.FinanceTracker.controller;

import com.example.FinanceTracker.model.Expense;
import com.example.FinanceTracker.model.DTO.ExpenseDTO;
import com.example.FinanceTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService service;

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseDTO dto, Authentication authentication) {
        return ResponseEntity.ok(
                service.addExpense(dto, authentication.getName())
        );
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(Authentication authentication) {
        return ResponseEntity.ok(
                service.getExpensesByUser(authentication.getName())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseDTO dto,
            Authentication authentication
    ) {
        service.updateExpense(id, dto, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Expense updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(
            @PathVariable Long id,
            Authentication authentication
    ) {
        service.deleteExpense(id, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Expense deleted"));
    }
}
