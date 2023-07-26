package com.boardify.boardify.service.impl;

import com.boardify.boardify.entities.Transaction;
import com.boardify.boardify.repository.TransactionRepository;
import com.boardify.boardify.service.TransactionService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

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

    public List<Transaction> findByFilter(Map<String, String> customQuery) {
        String item = customQuery.get("item");
        String type = customQuery.get("type");
        String startDate = customQuery.get("startDate");
        String endDate = customQuery.get("endDate");

        if (item.equals("All")) {
            item = "%";
        }
        if (type.equals("All")) {
            type = "%";
        }

        if (startDate == null || startDate.equals("")) {
            startDate = "01/01/2022";
        }
        if (endDate == null || endDate.equals("")) {
            endDate = LocalDate.now().toString();
        } else {
            endDate = convertDate(endDate);
        }

        return transactionRepository.findByFilter(item, type.toLowerCase(), convertDate(startDate), endDate);
    }

    private String convertDate (String dateFromQuery) {
        String[] date = dateFromQuery.split("/");
        String sqlDate = date[2] + "-" + date[0] + "-" + date[1];
//        int year = Integer.parseInt(date[2]) - 1900;
//        int month = Integer.parseInt(date[0]) - 1;
//        int day = Integer.parseInt(date[1]);
//        Date sqlDate = new Date(year, month, day);

        return sqlDate;
    }
}
