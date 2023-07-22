package com.boardify.boardify.repository;

import com.boardify.boardify.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByTransactionDate(Date transactionDate);

    List<Transaction> findAllByTransactionTimeBetween(Date transactionTimeStart, Date transactionTimeEnd);

    List<Transaction> findByUserId(Long userId);

    @Query(
            value="SELECT * FROM Transactions " +
                    "WHERE item LIKE :item " +
                    "AND transaction_type LIKE :type " +
                    "AND transaction_date >= :startDate " +
                    "AND transaction_date <= :endDate " +
                    "ORDER BY transaction_date desc, transaction_time desc ;",
            nativeQuery = true
    )
    List<Transaction> findByFilter(@Param("item") String item, @Param("type") String type, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
