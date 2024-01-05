package com.example.traveler.repository;

import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionIdAndAccountBook_accountId(Long transactionId, Long accountId); // 메서드 이름 변경
    Optional<Transaction> findById(Long transactionId);
    Optional<Transaction> findByTransactionIdAndAccountBook(Long transactionId, AccountBook accountBook);

    List<Transaction> findAllByAccountBook(AccountBook accountBook);
}