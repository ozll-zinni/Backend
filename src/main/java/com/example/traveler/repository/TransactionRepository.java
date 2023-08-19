package com.example.traveler.repository;

import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAccountBook(AccountBook accountBook);
}
