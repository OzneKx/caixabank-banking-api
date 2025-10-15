package com.hackathon.finservice.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import java.time.LocalTime;

@Entity
@Table(name = "investment_logs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestmentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account;

    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal previousBalance;

    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal interestApplied;

    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal newBalance;

    LocalTime appliedAt = LocalTime.now();
}
