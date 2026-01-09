package com.example.FinanceTracker.controller;

import com.example.FinanceTracker.model.Income;
import com.example.FinanceTracker.model.DTO.IncomeDTO;
import com.example.FinanceTracker.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService service;

    @PostMapping
    public ResponseEntity<Income> addIncome(@RequestBody IncomeDTO dto, Authentication authentication) {
        return ResponseEntity.ok(
                service.addIncome(dto, authentication.getName())
        );
    }

    @GetMapping
    public ResponseEntity<List<Income>> getIncomes(Authentication authentication) {
        return ResponseEntity.ok(
                service.getIncomesByUser(authentication.getName())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIncome(
            @PathVariable Long id,
            @RequestBody IncomeDTO dto,
            Authentication authentication
    ) {
        service.updateIncome(id, dto, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Income updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncome(
            @PathVariable Long id,
            Authentication authentication
    ) {
        service.deleteIncome(id, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Income deleted"));
    }
}
