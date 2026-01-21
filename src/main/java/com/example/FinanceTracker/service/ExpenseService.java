package com.example.FinanceTracker.service;

import com.example.FinanceTracker.model.DTO.ExpenseDTO;
import com.example.FinanceTracker.model.Expense;
import com.example.FinanceTracker.model.ExpenseCategory;
import com.example.FinanceTracker.model.User;
import com.example.FinanceTracker.repo.ExpenseRepository;
import com.example.FinanceTracker.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository repo;
    private final UserRepository userRepo;

    public Expense addExpense(ExpenseDTO dto, String email) {
        User user = userRepo.findByEmail(email).orElseThrow();

        Expense expense = new Expense();
        expense.setDescription(dto.getDescription());
        expense.setCategory(ExpenseCategory.valueOf(dto.getCategory()));
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be a positive number");
        }
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setUser(user);

        return repo.save(expense);
    }

    public List<Expense> getExpensesByUser(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return repo.findByUserId(user.getId());
    }

    public void updateExpense(Long id, ExpenseDTO dto, String email) {
        Expense expense = repo.findById(id).orElseThrow();

        if (!expense.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        expense.setDescription(dto.getDescription());
        expense.setCategory(ExpenseCategory.valueOf(dto.getCategory()));
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());

        repo.save(expense);
    }

    public void deleteExpense(Long id, String email) {
        Expense expense = repo.findById(id).orElseThrow();

        if (!expense.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        repo.delete(expense);
    }
}
