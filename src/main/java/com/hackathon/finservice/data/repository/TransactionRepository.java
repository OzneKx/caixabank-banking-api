package com.hackathon.finservice.data.repository;

import com.hackathon.finservice.data.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
        SELECT 
            transaction FROM Transaction transaction
        WHERE
            transaction.sourceAccount.user.id = :userId
        OR 
            transaction.targetAccount.user.id = :userId
        ORDER BY
            transaction.transactionDate DESC
    """)
    List<Transaction> findAllByUserId(@Param("userId") Long id);
}
