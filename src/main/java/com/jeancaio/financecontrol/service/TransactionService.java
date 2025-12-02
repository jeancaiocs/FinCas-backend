package com.jeancaio.financecontrol.service;

import com.jeancaio.financecontrol.model.Transaction;
import com.jeancaio.financecontrol.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findByUserId(Long userId, String type, Long categoryId, String startDate, String endDate) {
        // Se não tem filtros, retorna tudo do usuário
        if (type == null && categoryId == null && startDate == null && endDate == null) {
            return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
        }

        // Com filtros
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        return transactionRepository.findByFilters(userId, type, categoryId, start, end);
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }
}