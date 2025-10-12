package com.hackathon.finservice.service.impl;

import com.hackathon.finservice.data.entity.Account;
import com.hackathon.finservice.data.entity.Transaction;
import com.hackathon.finservice.data.entity.TransactionType;
import com.hackathon.finservice.data.entity.User;
import com.hackathon.finservice.data.mapper.TransactionMapper;
import com.hackathon.finservice.data.repository.AccountRepository;
import com.hackathon.finservice.data.repository.TransactionRepository;
import com.hackathon.finservice.data.repository.UserRepository;
import com.hackathon.finservice.dto.DepositRequest;
import com.hackathon.finservice.dto.MessageResponse;
import com.hackathon.finservice.dto.TransactionResponse;
import com.hackathon.finservice.dto.TransferRequest;
import com.hackathon.finservice.dto.WithdrawRequest;
import com.hackathon.finservice.service.TransactionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository,
                                  UserRepository userRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public MessageResponse cashDepositTransaction(DepositRequest depositRequest) {
        User user = getAuthenticatedUser();
        Account account = getMainAccountFromAuthenticatedUser(user);

        BigDecimal amount = depositRequest.amount();
        checkIfAmountIsValidForTransaction(amount);

        Transaction transaction = transactionMapper.toEntity(amount, TransactionType.CASH_DEPOSIT);
        transaction.setSourceAccount(account);
        transactionRepository.save(transaction);

        return new MessageResponse("Cash deposited successfully");
    }

    @Override
    public MessageResponse cashWithdrawTransaction(WithdrawRequest withdrawRequest) {
        User user = getAuthenticatedUser();
        Account account = getMainAccountFromAuthenticatedUser(user);

        BigDecimal amount = withdrawRequest.amount();
        checkIfAmountIsValidForTransaction(amount);

        if (checkIfAccountHasInsufficientBalance(account, amount)) {
            return new MessageResponse("Insufficient balance");
        }

        Transaction transaction = transactionMapper.toEntity(amount, TransactionType.CASH_WITHDRAW);
        transaction.setSourceAccount(account);
        transactionRepository.save(transaction);

        return new MessageResponse("Cash withdrawn successfully");
    }

    @Override
    public MessageResponse cashTransferTransaction(TransferRequest transferRequest) {
        User user = getAuthenticatedUser();
        Account source = getMainAccountFromAuthenticatedUser(user);
        Account target = getTargetAccountForTransfer(transferRequest);

        BigDecimal amount = transferRequest.amount();
        checkIfAmountIsValidForTransaction(amount);

        if (checkIfAccountHasInsufficientBalance(source, amount)) {
            return new MessageResponse("Insufficient balance");
        }

        Transaction transaction = transactionMapper.toEntity(amount, TransactionType.CASH_TRANSFER);
        transaction.setSourceAccount(source);
        transaction.setTargetAccount(target);
        transactionRepository.save(transaction);

        return new MessageResponse("Fund transferred successfully");
    }

    @Override
    public List<TransactionResponse> getUserTransactionHistory() {
        User user = getAuthenticatedUser();
        return transactionRepository.findAllByUserId(user.getId())
            .stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(
                BAD_REQUEST,
                "User not found for the given identifier: " + email
        ));
    }

    private Account getMainAccountFromAuthenticatedUser(User user) {
        return user.getAccounts().stream()
                .filter(account -> "Main".equalsIgnoreCase(account.getAccountType()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        BAD_REQUEST,
                        "Main account not found for the given identifier: " + user.getEmail()
                ));
    }

    private Account getTargetAccountForTransfer(TransferRequest transferRequest) {
        return accountRepository.findByAccountNumber(transferRequest.targetAccountNumber())
                .orElseThrow(() -> new RuntimeException("Target account not found"));
    }

    private void checkIfAmountIsValidForTransaction(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    private boolean checkIfAccountHasInsufficientBalance(Account account, BigDecimal amount) {
        return account.getBalance().compareTo(amount) < 0;
    }
}
