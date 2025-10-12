package com.hackathon.finservice.service;

import com.hackathon.finservice.data.entity.Account;
import com.hackathon.finservice.data.entity.InvestmentLog;
import com.hackathon.finservice.data.repository.AccountRepository;
import com.hackathon.finservice.data.repository.InvestmentLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvestmentInterestService {
    private final AccountRepository accountRepository;
    private final InvestmentLogRepository investmentLogRepository;

    public InvestmentInterestService(AccountRepository accountRepository, InvestmentLogRepository investmentLogRepository) {
        this.accountRepository = accountRepository;
        this.investmentLogRepository = investmentLogRepository;
    }

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void applyInterestRateToAccountBalance() {
        List<Account> investAccounts = getInvestAccountType();

        for (Account account : investAccounts) {
            BigDecimal previousBalance = account.getBalance();
            BigDecimal interestApplied = previousBalance.multiply(BigDecimal.valueOf(0.10))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal newBalance = previousBalance.add(interestApplied);

            account.setBalance(newBalance);
            accountRepository.save(account);

            InvestmentLog investmentLog = getInvestmentLog(account, previousBalance, interestApplied, newBalance);
            investmentLogRepository.save(investmentLog);
        }
    }

    private static InvestmentLog getInvestmentLog(Account account, BigDecimal previousBalance,
                                                  BigDecimal interestApplied, BigDecimal newBalance) {
        InvestmentLog investmentLog = new InvestmentLog();
        investmentLog.setAccount(account);
        investmentLog.setPreviousBalance(previousBalance);
        investmentLog.setInterestApplied(interestApplied);
        investmentLog.setNewBalance(newBalance);
        return investmentLog;
    }

    private List<Account> getInvestAccountType() {
        return accountRepository.findAll()
                .stream()
                .filter(a -> "Invest".equalsIgnoreCase(a.getAccountType())
                        && a.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .toList();
    };
}
