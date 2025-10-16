package com.hackathon.finservice.service;

import com.hackathon.finservice.dto.transaction.DepositRequest;
import com.hackathon.finservice.dto.transaction.MessageResponse;
import com.hackathon.finservice.dto.transaction.TransactionResponse;
import com.hackathon.finservice.dto.transaction.TransferRequest;
import com.hackathon.finservice.dto.transaction.WithdrawRequest;

import java.util.List;

public interface TransactionService {
    MessageResponse cashDepositTransaction(DepositRequest depositRequest);

    MessageResponse cashWithdrawTransaction(WithdrawRequest withdrawRequest);

    MessageResponse cashTransferTransaction(TransferRequest transferRequest);

    List<TransactionResponse> getUserTransactionHistory();
}
