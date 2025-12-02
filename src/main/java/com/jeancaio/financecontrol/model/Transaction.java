package com.jeancaio.financecontrol.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    private Long userId;

    @Column(nullable = false)
    private String type; // "income" ou "expense"

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private String description;

    @Column(name = "category_id")
    @JsonProperty("category_id")
    private Long categoryId;

    @Column(name = "transaction_date", nullable = false)
    @JsonProperty("transaction_date")
    private LocalDate transactionDate;

    // Relacionamento com categoria (para retornar os dados da categoria)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category categories;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }
}