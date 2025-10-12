package com.hackathon.finservice.service.impl;

import com.hackathon.finservice.data.entity.Account;
import com.hackathon.finservice.data.entity.Transaction;
import com.hackathon.finservice.data.entity.TransactionStatus;
import com.hackathon.finservice.data.entity.TransactionType;
import com.hackathon.finservice.data.repository.AccountRepository;
import com.hackathon.finservice.data.repository.TransactionRepository;
import com.hackathon.finservice.service.MonitoringService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class MonitoringServiceImpl implements MonitoringService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public MonitoringServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void runMonitoringCycle() {
        List<Transaction> pendingTransactions = transactionRepository.findAll()
                .stream()
                .filter(t -> t.getTransactionStatus() == TransactionStatus.PENDING)
                .toList();

        for (Transaction transaction : pendingTransactions) {
            switch (transaction.getTransactionType()) {
                case CASH_DEPOSIT -> processCashDepositTransaction(transaction);
                case CASH_WITHDRAW -> processCashWithdrawTransaction(transaction);
                case CASH_TRANSFER -> processCashTransferTransaction(transaction);
            }
        }
    }

    private void processCashDepositTransaction(Transaction transaction) {
        Account sourceAccount = transaction.getSourceAccount();
        BigDecimal amount = transaction.getAmount();

        BigDecimal fee = amount.compareTo(BigDecimal.valueOf(50000)) > 0
                ? amount.multiply(BigDecimal.valueOf(0.02)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal credited = amount.subtract(fee);
        sourceAccount.setBalance(sourceAccount.getBalance().add(credited));

        transaction.setFee(fee);
        transaction.setTransactionStatus(TransactionStatus.APPROVED);

        accountRepository.save(sourceAccount);
        transactionRepository.save(transaction);
    }

    private void processCashWithdrawTransaction(Transaction transaction) {
        Account sourceAccount = transaction.getSourceAccount();
        BigDecimal amount = transaction.getAmount();

        BigDecimal fee = amount.compareTo(BigDecimal.valueOf(10000)) > 0
                ? amount.multiply(BigDecimal.valueOf(0.01)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal total = amount.add(fee);

        if (sourceAccount.getBalance().compareTo(total) >= 0) {
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(total));
            transaction.setFee(fee);
            transaction.setTransactionStatus(TransactionStatus.APPROVED);
        } else {
            transaction.setTransactionStatus(TransactionStatus.FRAUD);
        }

        accountRepository.save(sourceAccount);
        transactionRepository.save(transaction);
    }

    private void processCashTransferTransaction(Transaction transaction) {
        Account sourceAccount = transaction.getSourceAccount();
        Account targetAccount = transaction.getTargetAccount();
        BigDecimal amount = transaction.getAmount();

        if (amount.compareTo(BigDecimal.valueOf(80000)) > 0) {
            transaction.setTransactionStatus(TransactionStatus.FRAUD);
            transactionRepository.save(transaction);
            return;
        }

        if (getCountRecentCashTransferTransaction(transaction, targetAccount) > 4) {
            transaction.setTransactionStatus(TransactionStatus.FRAUD);
            transactionRepository.save(transaction);
            return;
        }

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            transaction.setTransactionStatus(TransactionStatus.FRAUD);
            transactionRepository.save(transaction);
            return;
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));
        transaction.setTransactionStatus(TransactionStatus.APPROVED);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
        transactionRepository.save(transaction);
    }

    private long getCountRecentCashTransferTransaction(Transaction transaction, Account targetAccount) {
        long now = transaction.getTransactionDate().toEpochMilli();

        return transactionRepository.findAll().stream()
                .filter(t -> t.getTransactionType() == TransactionType.CASH_TRANSFER)
                .filter(t -> t.getTargetAccount() != null)
                .filter(t -> t.getTargetAccount().getId().equals(targetAccount.getId()))
                .filter(t -> Math.abs(now - t.getTransactionDate().toEpochMilli()) < 5000)
                .count();
    }
}
