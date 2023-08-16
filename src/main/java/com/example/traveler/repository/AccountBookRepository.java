package com.example.traveler.repository;

import com.example.traveler.model.entity.AccountBook;
import com.example.traveler.model.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {
    AccountBook findByTravel(Travel travel);

    Optional<AccountBook> findByAccountId(int accountId);

    List<AccountBook> findAllByTravel(Travel travel);
}
