package com.jeancaio.financecontrol.controller;

import com.jeancaio.financecontrol.model.Transaction;
import com.jeancaio.financecontrol.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Listar todas as transações do usuário
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(
            HttpServletRequest request,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long category_id,
            @RequestParam(required = false) String start_date,
            @RequestParam(required = false) String end_date
    ) {
        Long userId = (Long) request.getAttribute("userId");

        List<Transaction> transactions = transactionService.findByUserId(
                userId, type, category_id, start_date, end_date
        );

        return ResponseEntity.ok(transactions);
    }

    // Criar nova transação
    @PostMapping
    public ResponseEntity<?> createTransaction(
            HttpServletRequest request,
            @RequestBody Transaction transaction
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            transaction.setUserId(userId);

            Transaction saved = transactionService.save(transaction);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Erro ao criar transação: " + e.getMessage()));
        }
    }

    // Atualizar transação
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody Transaction transaction
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            Transaction existing = transactionService.findById(id);
            if (existing == null || !existing.getUserId().equals(userId)) {
                return ResponseEntity.status(404)
                        .body(Map.of("message", "Transação não encontrada"));
            }

            transaction.setId(id);
            transaction.setUserId(userId);

            Transaction updated = transactionService.save(transaction);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Erro ao atualizar transação: " + e.getMessage()));
        }
    }

    // Deletar transação
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            Transaction existing = transactionService.findById(id);
            if (existing == null || !existing.getUserId().equals(userId)) {
                return ResponseEntity.status(404)
                        .body(Map.of("message", "Transação não encontrada"));
            }

            transactionService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Transação deletada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Erro ao deletar transação: " + e.getMessage()));
        }
    }
}