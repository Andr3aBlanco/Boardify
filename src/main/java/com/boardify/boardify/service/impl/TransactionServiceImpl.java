package com.boardify.boardify.service.impl;

import com.boardify.boardify.entities.Transaction;
import com.boardify.boardify.repository.TransactionRepository;
import com.boardify.boardify.service.TransactionService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository transactionRepository;

    TransactionServiceImpl (TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findAllByTransactionDate(Date transactionDate) {
        return transactionRepository.findAllByTransactionDate(transactionDate);
    };

    public List<Transaction> findAllByTransactionTimeBetween(Date transactionTimeStart, Date transactionTimeEnd) {
        return transactionRepository.findAllByTransactionTimeBetween(transactionTimeStart, transactionTimeEnd);
    };

    public List<Transaction> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    };

    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }
}
