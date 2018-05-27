package com.company.cc.transaction.repository;

import com.company.cc.transaction.domain.Transaction;
import com.company.cc.transaction.service.dto.TransactionSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByAccountId(Long accountId);

    @Query(value = "SELECT " +
//            "SUM( CASE WHEN t.direction == 'IN' THEN t.amount ELSE (-1 * t.amount)  END )) " +
            "SUM( CASE WHEN t.direction IS 'IN' THEN t.amount ELSE -1*t.amount END ) " +
            "FROM transactions t WHERE t.customer_id = :customerId", nativeQuery = true)
    Optional<Double> getSummaryByCustomerId(@Param(value = "customerId") long customerId);

}
