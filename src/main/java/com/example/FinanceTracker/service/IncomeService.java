package com.example.FinanceTracker.service;

import com.example.FinanceTracker.model.DTO.IncomeDTO;
import com.example.FinanceTracker.model.Income;
import com.example.FinanceTracker.model.IncomeSource;
import com.example.FinanceTracker.model.User;
import com.example.FinanceTracker.repo.IncomeRepository;
import com.example.FinanceTracker.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository repo;
    private final UserRepository userRepo;

    public Income addIncome(IncomeDTO dto, String email) {
        User user = userRepo.findByEmail(email).orElseThrow();

        Income income = new Income();
        income.setDescription(dto.getDescription());
        income.setSource(IncomeSource.valueOf(dto.getSource()));
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new IllegalArgumentException("Income amount must be a positive number");
        }
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());
        income.setUser(user);

        return repo.save(income);
    }

    public List<Income> getIncomesByUser(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return repo.findByUserId(user.getId());
    }

    public void updateIncome(Long id, IncomeDTO dto, String email) {
        Income income = repo.findById(id).orElseThrow();

        if (!income.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        income.setDescription(dto.getDescription());
        income.setSource(IncomeSource.valueOf(dto.getSource()));
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());

        repo.save(income);
    }

    public void deleteIncome(Long id, String email) {
        Income income = repo.findById(id).orElseThrow();

        if (!income.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        repo.delete(income);
    }
}
