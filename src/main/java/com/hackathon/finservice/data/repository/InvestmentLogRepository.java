package com.hackathon.finservice.data.repository;

import com.hackathon.finservice.data.entity.Account;
import com.hackathon.finservice.data.entity.InvestmentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestmentLogRepository extends JpaRepository<InvestmentLog, Long> {
    List<InvestmentLog> findByAccountOrderByAppliedAtDesc(Account account);
}
