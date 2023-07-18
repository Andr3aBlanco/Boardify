package com.boardify.boardify.repository;

import com.boardify.boardify.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByTransactionDate(Date transactionDate);

    List<Transaction> findAllByTransactionTimeBetween(Date transactionTimeStart, Date transactionTimeEnd);

    List<Transaction> findByUserId(Long userId);
}
