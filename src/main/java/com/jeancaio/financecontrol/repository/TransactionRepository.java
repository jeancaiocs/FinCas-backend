package com.jeancaio.financecontrol.repository;

import com.jeancaio.financecontrol.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
            "AND (:type IS NULL OR t.type = :type) " +
            "AND (:categoryId IS NULL OR t.categoryId = :categoryId) " +
            "AND (:startDate IS NULL OR t.transactionDate >= :startDate) " +
            "AND (:endDate IS NULL OR t.transactionDate <= :endDate) " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> findByFilters(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}