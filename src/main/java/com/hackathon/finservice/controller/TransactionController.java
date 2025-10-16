package com.hackathon.finservice.controller;

import com.hackathon.finservice.dto.transaction.DepositRequest;
import com.hackathon.finservice.dto.transaction.MessageResponse;
import com.hackathon.finservice.dto.transaction.TransactionResponse;
import com.hackathon.finservice.dto.transaction.TransferRequest;
import com.hackathon.finservice.dto.transaction.WithdrawRequest;
import com.hackathon.finservice.service.MonitoringService;
import com.hackathon.finservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class TransactionController {
    private final TransactionService transactionService;
    private final MonitoringService monitoringService;

    public TransactionController(TransactionService transactionService, MonitoringService monitoringService) {
        this.transactionService = transactionService;
        this.monitoringService = monitoringService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<MessageResponse> cashDepositTransaction(@RequestBody DepositRequest depositRequest) {
        MessageResponse messageResponse = transactionService.cashDepositTransaction(depositRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<MessageResponse> cashWithdrawTransaction(@RequestBody WithdrawRequest withdrawRequest) {
        MessageResponse messageResponse = transactionService.cashWithdrawTransaction(withdrawRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/fund-transfer")
    public ResponseEntity<MessageResponse> cashTransferTransaction(@RequestBody TransferRequest transferRequest) {
        MessageResponse messageResponse = transactionService.cashTransferTransaction(transferRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getUserTransactionHistory() {
        List<TransactionResponse> transactions = transactionService.getUserTransactionHistory();
        return ResponseEntity.ok(transactions);
    }
}
