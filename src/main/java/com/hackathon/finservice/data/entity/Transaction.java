package com.hackathon.finservice.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    TransactionStatus transactionStatus = TransactionStatus.PENDING;

    @Column(nullable = false)
    BigDecimal amount;

    BigDecimal fee = BigDecimal.ZERO;

    Instant transactionDate = Instant.now();

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    Account targetAccount;
}