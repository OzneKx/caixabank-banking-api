package com.hackathon.finservice.service;

import com.hackathon.finservice.dto.DepositRequest;
import com.hackathon.finservice.dto.MessageResponse;
import com.hackathon.finservice.dto.TransactionResponse;
import com.hackathon.finservice.dto.TransferRequest;
import com.hackathon.finservice.dto.WithdrawRequest;

import java.util.List;

public interface TransactionService {
    MessageResponse cashDepositTransaction(DepositRequest depositRequest);

    MessageResponse cashWithdrawTransaction(WithdrawRequest withdrawRequest);

    MessageResponse cashTransferTransaction(TransferRequest transferRequest);

    List<TransactionResponse> getUserTransactionHistory();
}
