package com.example.FinanceTracker.repo;

import com.example.FinanceTracker.model.Expense;
import com.example.FinanceTracker.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId")
    List<Expense> findByUserId(@Param("userId") Long userId);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND e.category = :category")
    List<Expense> findByUserIdAndCategory(@Param("userId") Long userId, @Param("category") ExpenseCategory category);
}
