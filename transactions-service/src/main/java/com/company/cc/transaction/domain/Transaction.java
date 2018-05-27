package com.company.cc.transaction.domain;

import com.company.cc.transaction.domain.enumeration.TransactionDirection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity( name = "transactions")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 7799738024850692428L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "customer_id", nullable = false)
    @NotNull
    private Long customerId;

    @Column(name = "account_id", nullable = false)
    @NotNull
    private Long accountId;

    @Column(name = "amount", nullable = false)
    @NotNull
    private Double amount;

    @Column(name = "direction", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionDirection direction;

    public Transaction() {
    }

    public Transaction(@NotNull Long customerId, @NotNull Long accountId, @NotNull Double amount, @NotNull TransactionDirection direction) {
        this.customerId = customerId;
        this.accountId = accountId;
        this.amount = amount;
        this.direction = direction;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public Transaction setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Transaction setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Transaction setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public TransactionDirection getDirection() {
        return direction;
    }

    public Transaction setDirection(TransactionDirection direction) {
        this.direction = direction;
        return this;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Transaction setCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
